package com.lvmama.bms.scheduler.support;

import com.alibaba.fastjson.JSON;
import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.domain.dto.MessageDTO;
import com.lvmama.bms.core.domain.enums.MsgStatus;
import com.lvmama.bms.core.factory.NamedThreadFactory;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.store.mysql.MsgSerialize4DB;
import com.lvmama.bms.lock.RedisDistributedLock;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.store.MessageStore;
import com.lvmama.bms.scheduler.store.MsgTokenStore;
import com.lvmama.bms.scheduler.store.domain.po.MessagePO;
import com.lvmama.bms.scheduler.store.domain.po.MsgTokenPO;
import com.lvmama.bms.scheduler.store.domain.po.MsgTypePO;
import com.lvmama.bms.scheduler.store.domain.po.TokenPO;
import redis.clients.jedis.Pipeline;

import java.util.*;
import java.util.concurrent.*;

/**
 * 多级优先队列预加载&派发消息
 */
public class MessageDispatcher {

    private Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);

    private Integer preLoadSize = 500;

    private float preLoadRatio = 0.8f;

    private MsgCacheManager msgCacheManager;

    private MessageStore messageStore;

    private MsgTokenStore msgTokenStore;

    private RedisTemplate redisTemplate;

    private RedisDistributedLock redisDistributedLock;

    private int nloadThread = 30;

    public enum MessageSpeed {
        Fast, Slow
    }

    public MessageDispatcher(MsgSchedulerAppContext appContext) {
        msgCacheManager = appContext.getMsgCacheManager();
        messageStore = appContext.getMessageStore();
        msgTokenStore = appContext.getMsgTokenStore();
        redisTemplate = appContext.getRedisTemplate();
        redisDistributedLock = appContext.getRedisDistributedLock();
    }

    public void start() {

        final Semaphore resource = new Semaphore(nloadThread);
        final ExecutorService loadThread = Executors.newFixedThreadPool(nloadThread, new NamedThreadFactory("preloader"));
        ScheduledExecutorService executorService =  Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("preloader-timer"));
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                for(final MsgTypePO msgType : msgCacheManager.getMsgTypeOn()) {
                    try {
                        resource.acquire();
                        loadThread.submit(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    synchronized (("msgType"+String.valueOf(msgType.getId())).intern()) {
                                        load(msgType);
                                    }
                                } catch (Exception e) {
                                    logger.error("tip=fail preload message|msgType="+msgType.getType()+"|exception=", e);
                                } finally {
                                    resource.release();
                                }
                            }
                        });
                    } catch (Exception e) {
                        logger.error("tip=fail preload message|msgType="+msgType.getType()+"|exception=", e);
                    }
                }
            }
        }, 0, 30, TimeUnit.MILLISECONDS);

    }

    private void load(MsgTypePO msgType) {

        Set<Integer> slowTokens = msgCacheManager.getSlowMessage().get(msgType.getId());

        Set<Integer> tokenOff = msgCacheManager.getTokenOff().keySet();

        Set<Integer> invalidToken = msgCacheManager.getInvalidMsgTokenIdSet();

        //加载快消息
        Set<Integer> tokenExclude = new HashSet<Integer>();
        tokenExclude.addAll(tokenOff);
        tokenExclude.addAll(slowTokens);
        tokenExclude.addAll(invalidToken);
        load0(MessageSpeed.Fast, msgType, null, tokenExclude);

        if(slowTokens.size() > 0) {
            //加载慢消息

            Set<Integer> tokenInclude = new HashSet<>();

            for(Iterator<Integer> iterator = slowTokens.iterator(); iterator.hasNext();) {
                Integer tokenId = iterator.next();
                if(!tokenOff.contains(tokenId)) {
                    tokenInclude.add(tokenId);
                }
            }

            if(tokenInclude.size() > 0) {
                load0(MessageSpeed.Slow, msgType, tokenInclude, null);
            }

        }

    }


    private void load0(MessageSpeed speed, MsgTypePO msgType, Set<Integer> tokenInclude, Set<Integer> tokenExclude) {

        long start = System.currentTimeMillis();

        Integer msgTypeId = msgType.getId();

        String queueName = redisTemplate.getCacheKey(speed.name(), String.valueOf(msgTypeId));

        int loadSize = redisTemplate.llen(queueName).intValue();

        if(loadSize * 1f / preLoadSize < preLoadRatio) {

            List<MsgTokenPO> msgTokens = msgTokenStore.getMsgToken(tokenInclude, tokenExclude,
                    preLoadSize-loadSize, msgTypeId, MsgStatus.RECEIVE.ordinal());

            if(msgTokens.size() == 0) {
                return;
            }

            //消息id去重
            Set<Long> msgIds = new HashSet<Long>();
            for(MsgTokenPO msgToken : msgTokens) {
                msgIds.add(msgToken.getMsgId());
            }

            messageStore.batchUpdateStatus(msgIds, MsgStatus.RECEIVE.ordinal(), MsgStatus.SENDING.ordinal(), msgTypeId);
            Map<String, MessagePO> messageMap = messageStore.getMessage(msgIds, msgTypeId);

            List<MessageDTO> messageQueue = new LinkedList<MessageDTO>();

            for(MsgTokenPO msgToken : msgTokens) {
                TokenPO tokenPO = msgCacheManager.getToken(msgToken.getTokenId());
                if (tokenPO == null) {
                    msgCacheManager.addInvalidMsgTokenId(msgToken.getTokenId());
                    continue;
                }
                MessagePO messagePO = messageMap.get(msgToken.getMsgId());
                if(messagePO == null) {
                    continue;
                }
                MessageDTO message = wrapToMessage(messagePO, tokenPO, msgToken.getId(), msgToken.getRetryCount());
                int index = Collections.binarySearch(messageQueue, message, new Comparator<MessageDTO>() {
                    @Override
                    public int compare(MessageDTO prev, MessageDTO next) {
                        if(next.getPriority() > prev.getPriority()) {
                            return -1;
                        } else if(next.getPriority().equals(prev.getPriority())) {
                            if(next.getReceiveTime().getTime() > prev.getReceiveTime().getTime()) {
                                return -1;
                            } else {
                                return 1;
                            }
                        } else {
                            return 1;
                        }
                    }
                });

                messageQueue.add(-index-1, message);
            }

            if(messageQueue.size() > 0) {
                String[] messageJsonQueue = new String[messageQueue.size()];
                for(int index = 0; index < messageQueue.size(); index++) {
                    messageJsonQueue[index] = JSON.toJSONString(messageQueue.get(index));
                }
                redisTemplate.rpush(queueName, messageJsonQueue);

                //更新消息状态
                msgTokenStore.batchUpdateStatus(msgTokens, MsgStatus.SENDING.ordinal(), msgTypeId);
            }

            logger.info("tip=preload {} message|msgType={}|tokenInclude={}|tokenExclude={}|loadSize={}|costTime={}ms",
                    speed.name(), msgType.getType(), tokenInclude, tokenExclude, messageQueue.size(), System.currentTimeMillis()-start);

        }



    }


    public MessageDTO getNextMessage(final MessageSpeed speed) {

        //从开启推送&非空的队列获取消息
        final Map<Integer, MsgTypePO> candidates = new HashMap<Integer, MsgTypePO>();
        List<Object> resultList = redisTemplate.batch(new RedisTemplate.Batch() {
            @Override
            public List<Object> doBatch(Pipeline pipeline) {
                int index = 0;
                for(MsgTypePO msgType : msgCacheManager.getMsgTypeOn()) {
                    pipeline.llen(redisTemplate.getCacheKey(speed.name(), String.valueOf(msgType.getId())));
                    candidates.put(index++, msgType);
                }
                if(candidates.size() > 0) {
                    return pipeline.syncAndReturnAll();
                } else {
                    return Collections.emptyList();
                }
            }
        });

        int index = 0, sum = 0;
        for(Object result : resultList) {
            Long count = (Long) result;
            if(count > 0) {
                sum+=candidates.get(index++).getPriority();
            } else {
                candidates.remove(index++);
            }
        }

        if(candidates.size() == 0) {
            return null;
        }

        //加权随机
        Random random = new Random();
        int randomInt = random.nextInt(sum);

        int itemSum = 0;
        MsgTypePO msgType = null;
        for(MsgTypePO candidate : candidates.values()) {
            itemSum += candidate.getPriority();
            if(randomInt <= itemSum) {
                msgType = candidate;
                break;
            }
        }

        String messageStr = redisTemplate.lpop(speed.name(), String.valueOf(msgType.getId()));
        if(StringUtils.isEmpty(messageStr)) {
            return getNextMessage(speed);
        }

        MessageDTO message = JSON.parseObject(messageStr, MessageDTO.class);

        //过滤推送关闭的消息
        if(msgCacheManager.isTokenOff(message.getToken())) {
            msgTokenStore.updateStatus(message.getMsgTokenId(), MsgStatus.RECEIVE.ordinal(), message.getMsgTypeId());
            return getNextMessage(speed);
        }

        //限制消息推送频率
        if(message.getThrehold()!=null && message.getThrehold()!=0) {
            String rateLimit = redisTemplate.getCacheKey("RATE_LIMIT", message.getToken());
            Long totalCount = redisTemplate.increx(rateLimit, 1);
            if(totalCount > message.getThrehold()) {
                Long ttl = redisTemplate.pttl(rateLimit);
                if(ttl!=null && ttl > 0) {
                    msgCacheManager.turnOffToken(message.getToken(), ttl, TimeUnit.MILLISECONDS);
                    msgTokenStore.updateStatus(message.getMsgTokenId(), MsgStatus.RECEIVE.ordinal(), message.getMsgTypeId());
                    logger.info("tip=rate limit|token={}|threshold={}|totalCount={}|ttl={}", message.getToken(), message.getThrehold(), totalCount, ttl);
                    return getNextMessage(speed);
                }
            }
        }

        //根据队列类型过滤快慢消息
        if(MessageSpeed.Fast.equals(speed)
                && msgCacheManager.isSlowMessage(message.getMsgTypeId(), message.getToken())) {
            redisTemplate.rpush(new String[]{String.valueOf(msgType.getId()), MessageSpeed.Slow.name()}, messageStr);
            return getNextMessage(speed);
        }

        message.setSpeedFast(MessageSpeed.Fast.equals(speed));

        if(!wrapTokenToMessage(message)) {
            msgTokenStore.updateStatus(message.getMsgTokenId(), MsgStatus.RECEIVE.ordinal(), message.getMsgTypeId());
            return getNextMessage(speed);
        }

        return message;
    }

    private MessageDTO wrapToMessage(MessagePO messagePO, TokenPO tokenPO, Long msgTokenId, Integer retryCount) {

        MessageDTO messageDTO = new MessageDTO();

        messageDTO.setId(messagePO.getId());
        messageDTO.setMsgTokenId(msgTokenId);
        messageDTO.setMsgId(messagePO.getMsgId());
        messageDTO.setMsgTypeId(messagePO.getMsgTypeId());
        MsgTypePO msgType = msgCacheManager.getMsgType(messageDTO.getMsgTypeId());
        messageDTO.setMsgType(msgType.getType());
        messageDTO.setMsgContent(MsgSerialize4DB.unserialize(messagePO.getMsgContent()));
        messageDTO.setMaxRetry(messagePO.getMaxRetry());
        messageDTO.setRetryCount(retryCount);
        messageDTO.setReceiveTime(messagePO.getReceiveTime());
        messageDTO.setToken(tokenPO.getToken());
        messageDTO.setPriority(tokenPO.getPriority());
        messageDTO.setThrehold(tokenPO.getThreshold());

        return messageDTO;

    }

    private boolean wrapTokenToMessage(MessageDTO messageDTO) {

        TokenPO tokenPO = msgCacheManager.getTokenOn(messageDTO.getToken());
        if(tokenPO == null) {
            return false;
        }

        messageDTO.setPushType(tokenPO.getPushType());
        messageDTO.setPushUrl(tokenPO.getPushUrl());
        messageDTO.setConverterType(tokenPO.getConverterType());
        messageDTO.setThrehold(tokenPO.getThreshold());
        messageDTO.setRpcPusherId(tokenPO.getRpcPusherId());
        messageDTO.setConvertMapId(tokenPO.getConvertId());
        messageDTO.setEncryptKey(tokenPO.getEncryptKey());
        messageDTO.setEncryptMethod(tokenPO.getEncryptMethod());
        messageDTO.setConnectTimeOut(tokenPO.getConnectTimeOut());
        messageDTO.setReadTimeOut(tokenPO.getReadTimeOut());

        return true;

    }

}

package com.lvmama.bms.scheduler.support;

import com.alibaba.fastjson.JSON;
import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.bms.core.cluster.sync.SyncHandler;
import com.lvmama.bms.core.cluster.sync.SyncListener;
import com.lvmama.bms.core.commons.concurrent.ConcurrentHashSet;
import com.lvmama.bms.core.domain.enums.OpenStatus;
import com.lvmama.bms.core.factory.NamedThreadFactory;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.store.MsgTokenStore;
import com.lvmama.bms.scheduler.store.MsgTypeStore;
import com.lvmama.bms.scheduler.store.TokenStore;
import com.lvmama.bms.scheduler.store.domain.dto.SlowMessage;
import com.lvmama.bms.scheduler.store.domain.dto.TokenSwitch;
import com.lvmama.bms.scheduler.store.domain.po.MsgTypePO;
import com.lvmama.bms.scheduler.store.domain.po.TokenPO;
import com.lvmama.bms.scheduler.support.assign.TaskAssigner;
import com.lvmama.bms.scheduler.support.assign.TaskType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MsgCacheManager {

    private Logger logger = LoggerFactory.getLogger(MsgCacheManager.class);

    /**
     * 接收方
     * token, tokenPO
     */
    private Map<String, TokenPO> tokenMap = new ConcurrentHashMap<String, TokenPO>();

    /**
     * 接收方
     * tokenId, tokenPO
     */
    private Map<Integer, TokenPO> keyTokenMap = new ConcurrentHashMap<Integer, TokenPO>();

    /**
     * 消息类型
     */
    private Map<String, MsgTypePO> msgTypeMap = new ConcurrentHashMap<String, MsgTypePO>();
    /**
     * 慢消息
     * key: msgTypeId
     * value: token
     */
    private Map<Integer, ConcurrentHashSet<Integer>> slowMessageMap = new ConcurrentHashMap<Integer, ConcurrentHashSet<Integer>>();

    /**
     * 推送挂起的token
     */
    private Set<String> suspendToken = new ConcurrentHashSet<String>();

    /**
     * 已删除的消息类型
     */
    private Set<Integer> deleteMsgTypeSet = new ConcurrentHashSet<Integer>();
    /**
     * 已删除的接收方
     */
    private Set<String> deleteTokenSet = new ConcurrentHashSet<String>();

    /**
     * msg_token中无效的token_id
     */
    private Set<Integer> invalidMsgTokenIdSet = new ConcurrentHashSet<>();

    private MsgTypeStore msgTypeStore;

    private TokenStore tokenStore;

    private SyncHandler syncHandler;

    private ScheduledExecutorService executorService;

    private MsgTokenStore msgTokenStore;

    private RedisTemplate redisTemplate;

    private TaskAssigner taskAssigner;

    private volatile boolean isEnableTaskAssign = true;

    private MsgSchedulerAppContext appContext;

    public MsgCacheManager(MsgSchedulerAppContext appContext) {
        msgTypeStore = appContext.getMsgTypeStore();
        tokenStore = appContext.getTokenStore();
        syncHandler = appContext.getSyncHandler();
        executorService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("MsgCacheManager"));
        msgTokenStore = appContext.getMsgTokenStore();
        redisTemplate = appContext.getRedisTemplate();
        taskAssigner = appContext.getTaskAssigner();
        this.appContext = appContext;
    }

    public void start() {

        List<MsgTypePO> msgTypeList = msgTypeStore.queryAllMsgType();
        for(MsgTypePO msgType : msgTypeList) {
            msgTypeMap.put(msgType.getType(), msgType);
            slowMessageMap.put(msgType.getId(), new ConcurrentHashSet<Integer>());
        }

        List<TokenPO> tokenList = tokenStore.queryAllMsgToken();

        Map<Integer, TokenPO> tokenGroupMap = new HashMap<Integer, TokenPO>();
        for(TokenPO token : tokenList) {
            if(token.getGroupId()==null) {
                tokenGroupMap.put(token.getId(), token);
            } else {
                tokenMap.put(token.getToken(), token);
            }
        }

        for(TokenPO tokenPO : tokenMap.values()) {
            buildToken(tokenPO, tokenGroupMap.get(tokenPO.getGroupId()));
            keyTokenMap.put(tokenPO.getId(), tokenPO);
        }

        syncHandler.addListener(new SyncListener() {
            @Override
            public void onSyncEvent(SyncEvent syncEvent) {
                logger.info("tip=sync message config|"+syncEvent);
                flush(syncEvent);
            }
        });

    }

    public void flush(SyncEvent event) {
        switch (event.getObjectType()) {
            case ACCESS:
                if(SyncEvent.EventType.Reload.equals(event.getEventType())) {
                    flushAllToken();
                } else {
                    flushToken(event.getEventType(), event.getAddition());
                }
                break;
            case MSG_TYPE:
                flushMsgType(event.getEventType(), event.getAddition());
                break;
            case SLOW_MSG:
                flushSlowMessage(event.getEventType(), event.getAddition());
                break;
            case TOKEN_SWITCH:
                flushTokenSwitch(event.getEventType(), event.getAddition());
                break;
            case FLUSH_REDIS:
                flushRedis(event.getEventType(), event.getAddition());
                break;
            case TASK_ASSIGN:
                flushTaskAssign(event.getEventType(), event.getAddition());
                break;
        }
    }

    private void flushTaskAssign(SyncEvent.EventType eventType, String addition) {

        switch (eventType) {
            case TurnOn:
                isEnableTaskAssign = true;
                break;
            case TurnOff:
                isEnableTaskAssign = false;
                break;
            case Assign: //管理后台触发
                taskAssigner.assignTask();
                break;
            case Reload:
                taskAssigner.flushTaskAssign(addition);
                break;
        }

    }

    private void flushRedis(SyncEvent.EventType eventType, String addition) {
        switch (eventType) {
            case All:
                redisTemplate.flushAll();
                break;
        }
    }

    private void flushToken(SyncEvent.EventType eventType, String addition) {

        TokenPO newToken = JSON.parseObject(addition, TokenPO.class);
        synchronized (newToken.getToken().intern()) {
            switch (eventType) {
                case Add:
                    if(!deleteTokenSet.contains(newToken.getToken())) {
                        if(tokenMap.get(newToken.getToken())==null) {
                            tokenMap.put(newToken.getToken(), wrapDefaultToToken(newToken));
                            keyTokenMap.put(newToken.getId(), newToken);
                        }
                    }
                    break;
                case Update:
                    if(!deleteTokenSet.contains(newToken.getToken())) {
                        TokenPO oldToken = tokenMap.get(newToken.getToken());
                        if(oldToken==null) {
                            tokenMap.put(newToken.getToken(), newToken);
                            keyTokenMap.put(newToken.getId(), newToken);
                        } else {
                            if(newToken.getToken().equals(oldToken.getToken())) {
                                if(newToken.getVersion() >= oldToken.getVersion()) {
                                    tokenMap.put(newToken.getToken(), wrapDefaultToToken(newToken));
                                    keyTokenMap.put(newToken.getId(), newToken);
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case Delete:
                    tokenMap.remove(newToken.getToken());
                    keyTokenMap.remove(newToken.getId());
                    deleteTokenSet.add(newToken.getToken());
//                    deleteExpiredMsgToken(newToken);
                    break;
                case Reload:
                    flushAllToken();
                    break;

            }
        }

    }

    private void flushMsgType(SyncEvent.EventType eventType, String addition) {

        MsgTypePO newMsgType = JSON.parseObject(addition, MsgTypePO.class);
        synchronized ((MsgTypePO.class.getSimpleName()+String.valueOf(newMsgType.getId())).intern()) {
            switch (eventType) {
                case Add:
                    if(!deleteMsgTypeSet.contains(newMsgType.getId())) {
                        boolean isExist = false;
                        for(Iterator<MsgTypePO> iterator = msgTypeMap.values().iterator(); iterator.hasNext();) {
                            MsgTypePO oldMsgType = iterator.next();
                            if(newMsgType.getId().equals(oldMsgType.getId())) {
                                isExist = true;
                            }
                        }
                        if(!isExist) {
                            slowMessageMap.put(newMsgType.getId(), new ConcurrentHashSet<Integer>());
                            msgTypeMap.put(newMsgType.getType(), newMsgType);
                        }
                    }
                    break;
                case Update:
                    if(!deleteMsgTypeSet.contains(newMsgType.getId())) {
                       boolean isExist = false;
                        for(Iterator<MsgTypePO> iterator = msgTypeMap.values().iterator(); iterator.hasNext();) {
                            MsgTypePO oldMsgType = iterator.next();
                            if(newMsgType.getId().equals(oldMsgType.getId())) {
                                if(newMsgType.getVersion() >= oldMsgType.getVersion()) {
                                    msgTypeMap.remove(oldMsgType.getType());
                                    msgTypeMap.put(newMsgType.getType(), newMsgType);
                                }
                            }
                        }
                        if(!isExist) {
                            msgTypeMap.put(newMsgType.getType(), newMsgType);
                        }
                    }
                    break;
                case Delete:
                    for(Iterator<MsgTypePO> iterator = msgTypeMap.values().iterator(); iterator.hasNext();) {
                        MsgTypePO oldMsgType = iterator.next();
                        if(newMsgType.getId().equals(oldMsgType.getId())) {
                            iterator.remove();
                        }
                    }
                    deleteMsgTypeSet.add(newMsgType.getId());
                    slowMessageMap.remove(newMsgType.getId());
                    break;
            }

        }

        taskAssigner.assignTask();

    }

    private void flushSlowMessage(SyncEvent.EventType eventType, String addition) {

        SlowMessage slowMessage = JSON.parseObject(addition, SlowMessage.class);
        Set<Integer> slowToken = slowMessageMap.get(slowMessage.getMsgTypeId());

        switch (eventType) {
            case Add:
                slowToken.add(slowMessage.getTokenId());
                break;
            case Delete:
                slowToken.remove(slowMessage.getTokenId());
                break;
        }

    }

    private void flushTokenSwitch(SyncEvent.EventType eventType, String addition) {

        TokenSwitch tokenSwitch = JSON.parseObject(addition, TokenSwitch.class);

        switch(eventType) {
            case TurnOn:
                turnOnToken(tokenSwitch.getToken());
                break;
            case TurnOff:
                turnOffToken(tokenSwitch.getToken(), tokenSwitch.getRecoverTime());
                break;
        }

    }

    public Set<Integer> getInvalidMsgTokenIdSet() {
        return invalidMsgTokenIdSet;
    }

    public boolean addInvalidMsgTokenId(Integer tokenId) {
        return invalidMsgTokenIdSet.add(tokenId);
    }

    /**
     * 熔断->通知->恢复
     * @param token
     * @param recoverTime
     */
    public void turnOffAndSyncToken(String token, int recoverTime) {
        if(turnOffToken(token, recoverTime)) {
            TokenSwitch tokenSwitch = new TokenSwitch(token, recoverTime);
            SyncEvent syncEvent = new SyncEvent(SyncEvent.ObjectType.TOKEN_SWITCH, SyncEvent.EventType.TurnOff, JSON.toJSONString(tokenSwitch));
            syncHandler.sync(syncEvent);
        }
    }

    public boolean turnOffToken(String token, long recoverTime) {
        return turnOffToken(token, recoverTime, TimeUnit.SECONDS);
    }

    public boolean turnOffToken(final String token, long recoverTime, TimeUnit timeUnit) {

        TokenPO tokenPO = getToken(token);
        if(tokenPO!=null && tokenPO.getIsOpen() == OpenStatus.Open.ordinal()) {
            suspendToken.add(token);
            try {
                executorService.schedule(new Runnable() {
                    @Override
                    public void run() {
                        turnOnToken(token);
                        logger.info("Recover break push, the token is {}", token);
                    }
                }, recoverTime, timeUnit);
            } catch (Exception e) {
                suspendToken.remove(token);
            }

            return true;
        }
        return false;
    }

    public void turnOnToken(String token) {
        suspendToken.remove(token);
    }

    public boolean isSuspendToken(String token) {
        return suspendToken.contains(token);
    }

    public MsgTypePO getMsgType(String msgType) {
        return msgTypeMap.get(msgType);
    }

    public MsgTypePO getMsgType(Integer msgTypeId) {

        for(Iterator<MsgTypePO> iterator = msgTypeMap.values().iterator(); iterator.hasNext();) {
            MsgTypePO msgTypePO = iterator.next();
            if(msgTypePO.getId().equals(msgTypeId)) {
                return msgTypePO;
            }
        }

        return null;

    }

    public void addSlowMessage(Integer msgTypeId, String token) {
        TokenPO tokenPO = tokenMap.get(token);
        if(tokenPO != null) {
            Set<Integer> tokenSet = slowMessageMap.get(msgTypeId);
            tokenSet.add(tokenPO.getId());
            SlowMessage slowMessage = new SlowMessage(msgTypeId, tokenPO.getId());
            SyncEvent syncEvent = new SyncEvent(SyncEvent.ObjectType.SLOW_MSG, SyncEvent.EventType.Add, JSON.toJSONString(slowMessage));
            syncHandler.sync(syncEvent);
        }
    }

    public void removeSlowMessage(Integer msgTypeId, String token) {
        TokenPO tokenPO = tokenMap.get(token);
        if(tokenPO != null) {
            Set<Integer> tokenSet = slowMessageMap.get(msgTypeId);
            tokenSet.remove(tokenPO.getId());

            SlowMessage slowMessage = new SlowMessage(msgTypeId, tokenPO.getId());
            SyncEvent syncEvent = new SyncEvent(SyncEvent.ObjectType.SLOW_MSG, SyncEvent.EventType.Delete, JSON.toJSONString(slowMessage));
            syncHandler.sync(syncEvent);
        }
    }


    public Set<MsgTypePO> getUnAssignMsgTypeOn() {

        Set<MsgTypePO> msgTypeOn = new HashSet<>();

        for(Iterator<MsgTypePO> iterator = msgTypeMap.values().iterator(); iterator.hasNext();) {
            MsgTypePO msgType = iterator.next();
            if(msgType.getIsOpen() == OpenStatus.Open.ordinal()) {
                msgTypeOn.add(msgType);
            }
        }

        return msgTypeOn;
    }

    public Set<MsgTypePO> getMsgTypeOn() {
        if(isEnableTaskAssign) {
            return taskAssigner.acquireTask(TaskType.MsgPreload);
        } else {
            if(appContext.isMaster()) {
                return getUnAssignMsgTypeOn();
            } else {
                return Collections.EMPTY_SET;
            }
        }
    }

    public Collection<MsgTypePO> getAllMsgType() {
        return msgTypeMap.values();
    }

    public TokenPO getTokenOn(String token) {

        TokenPO tokenPO = tokenMap.get(token);
        if(tokenPO.getIsOpen() == OpenStatus.Open.ordinal() && !suspendToken.contains(token)) {
            return tokenPO;
        }

        return null;

    }

    public Map<Integer, TokenPO> getTokenOff() {

        Map<Integer, TokenPO> tokenOff = new HashMap<Integer, TokenPO>();

        for(Iterator<TokenPO> iterator = tokenMap.values().iterator(); iterator.hasNext();) {
            TokenPO token = iterator.next();
            if(token.getIsOpen() == OpenStatus.Close.ordinal() || suspendToken.contains(token.getToken())) {
                tokenOff.put(token.getId(), token);
            }
        }

        return tokenOff;
    }

    public boolean isTokenOff(String token) {

        TokenPO tokenPO = tokenMap.get(token);

        if(tokenPO != null && (tokenPO.getIsOpen() == OpenStatus.Close.ordinal() || suspendToken.contains(token))) {
            return true;
        }

        return false;

    }

    public TokenPO getToken(String token) {
        return tokenMap.get(token);
    }

    public TokenPO getToken(Integer tokenId) {
        return keyTokenMap.get(tokenId);
    }

    public CheckResult checkToken(String... tokens) {

        CheckResult checkResult = new CheckResult();

        List<String> invalidToken = new ArrayList<>();
        List<Integer> validToken = new ArrayList<>();

        for(String token : tokens) {
            TokenPO tokenPO = tokenMap.get(token);
            if(tokenPO!=null) {
                validToken.add(tokenPO.getId());
            } else {
                invalidToken.add(token);
            }
        }

        checkResult.setInvalidToken(invalidToken);
        checkResult.setValidToken(validToken);

        if(invalidToken.size() > 0 || validToken.size() == 0) {
            checkResult.setValid(false);
        }

        return checkResult;
    }

    public class CheckResult {

        private boolean isValid = true;

        private List<Integer> validToken = new ArrayList<>();

        private List<String> invalidToken = new ArrayList<>();

        public boolean isValid() {
            return isValid;
        }

        public void setValid(boolean valid) {
            isValid = valid;
        }

        public List<Integer> getValidToken() {
            return validToken;
        }

        public void setValidToken(List<Integer> validToken) {
            this.validToken = validToken;
        }

        public List<String> getInvalidToken() {
            return invalidToken;
        }

        public void setInvalidToken(List<String> invalidToken) {
            this.invalidToken = invalidToken;
        }

    }


    public Map<Integer, ConcurrentHashSet<Integer>> getSlowMessage() {
        return slowMessageMap;
    }

    public boolean isSlowMessage(Integer msgTypeId, String token) {

        Set<Integer> slowMessage = slowMessageMap.get(msgTypeId);

        TokenPO tokenPO = tokenMap.get(token);
        if(tokenPO != null && slowMessage.contains(tokenPO.getId())) {
            return true;
        }

        return false;

    }

    private void buildToken(TokenPO token, TokenPO tokenGroup) {

        if(token.getIsOpen()==null) {
            token.setIsOpen(tokenGroup.getIsOpen());
        }

        if(token.getConverterType()==null) {
            token.setConverterType(tokenGroup.getConverterType());
        }

        if(token.getPriority()==null) {
            token.setPriority(tokenGroup.getPriority());
        }

        if(token.getName()==null) {
            token.setName(tokenGroup.getName());
        }

        if(token.getPushType()==null) {
            token.setPushType(tokenGroup.getPushType());
        }

        if(token.getPushUrl()==null) {
            token.setPushUrl(tokenGroup.getPushUrl());
        }

        if(token.getThreshold()==null) {
            token.setThreshold(tokenGroup.getThreshold());
        }

        if(token.getReceiveType()==null) {
            token.setReceiveType(tokenGroup.getReceiveType());
        }

        if(token.getFailRate()==null) {
            token.setFailRate(tokenGroup.getFailRate());
        }

        if(token.getRpcPusherId()==null) {
            token.setRpcPusherId(tokenGroup.getRpcPusherId());
        }

        wrapDefaultToToken(token);

    }

    private TokenPO wrapDefaultToToken(TokenPO token) {

        if(token.getIsOpen()==null) {
            token.setIsOpen(OpenStatus.Close.ordinal());
        }

        if(token.getPriority()==null) {
            token.setPriority(0);
        }

        return token;
    }

    private void deleteExpiredMsgToken(TokenPO token) {

        for(Iterator<String> iterator = msgTypeMap.keySet().iterator(); iterator.hasNext();) {
            MsgTypePO msgType = msgTypeMap.get(iterator.next());
            int count = msgTokenStore.deleteExpiredMsgToken(token.getId(), msgType.getId());
            logger.info("tip=delete expired message token|msgType={}|token={}|count={}", msgType.getType(), token.getToken(), count);
        }

    }

    private void flushAllToken() {

        Map<String, TokenPO> tokenMap = new ConcurrentHashMap<>();
        Map<Integer, TokenPO> keyTokenMap = new ConcurrentHashMap<>();

        List<TokenPO> tokenList = tokenStore.queryAllMsgToken();

        Map<Integer, TokenPO> tokenGroupMap = new HashMap<Integer, TokenPO>();
        for(TokenPO token : tokenList) {
            if(token.getGroupId()==null) {
                tokenGroupMap.put(token.getId(), token);
            } else {
                tokenMap.put(token.getToken(), token);
            }
        }

        for(TokenPO tokenPO : tokenMap.values()) {
            buildToken(tokenPO, tokenGroupMap.get(tokenPO.getGroupId()));
            keyTokenMap.put(tokenPO.getId(), tokenPO);
        }

        this.tokenMap = tokenMap;
        this.keyTokenMap = keyTokenMap;
        this.deleteTokenSet = new ConcurrentHashSet<>();
        this.suspendToken = new ConcurrentHashSet<>();
        this.invalidMsgTokenIdSet = new ConcurrentHashSet<>();

        for(MsgTypePO msgType : msgTypeMap.values()) {
            this.slowMessageMap.put(msgType.getId(), new ConcurrentHashSet<Integer>());
        }

    }


}

package com.lvmama.bms.pusher.support;

import com.alibaba.fastjson.JSON;
import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.bms.core.cluster.sync.SyncHandler;
import com.lvmama.bms.core.cluster.sync.SyncListener;
import com.lvmama.bms.core.commons.concurrent.ConcurrentHashSet;
import com.lvmama.bms.core.commons.utils.JAXBUtils;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.domain.enums.ConvertVersion;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.extend.rpc.RpcPusher;
import com.lvmama.bms.pusher.domain.MsgPusherAppContext;
import com.lvmama.bms.pusher.map.v1.RequestMapBuilder;
import com.lvmama.bms.pusher.map.vo.ResponseMap;
import com.lvmama.bms.pusher.map.v1.vo.DataMap;
import com.lvmama.bms.pusher.protocol.domain.ConvertMapper;
import com.lvmama.bms.pusher.protocol.domain.PusherLoader;
import com.lvmama.bms.pusher.protocol.rpc.RpcClassLoader;
import com.lvmama.bms.pusher.runner.RunnerPool;
import com.lvmama.bms.pusher.store.ConvertMapStore;
import com.lvmama.bms.pusher.store.MsgPusherStore;
import com.lvmama.bms.pusher.store.domain.ConvertMapPO;
import com.lvmama.bms.pusher.store.domain.MsgPusherPO;
import org.apache.commons.io.IOUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MsgCacheManager {

    private Logger logger = LoggerFactory.getLogger(MsgCacheManager.class);

    private MsgPusherAppContext appContext;

    private SyncHandler syncHandler;

    private MsgPusherStore msgPusherStore;

    private ConvertMapStore convertMapStore;

    /**
     *  推送器jar本地存储路径
     */
    private String MsgPusherClassPathRoot;

    /**
     *  推送器缓存
     */
    private Map<Integer, PusherLoader> msgPusherMap = new ConcurrentHashMap<>();


    /**
     * 消息转换映射器
     */
    private Map<Integer, ConvertMapper> convertMapCache = new ConcurrentHashMap<>();

    /**
     *  已删除推送器id
     */
    private Set<Integer> deleteMsgPusherSet = new ConcurrentHashSet<>();

    private Set<Integer> deleteConvertMapSet = new ConcurrentHashSet<>();

    public MsgCacheManager(MsgPusherAppContext appContext) {
        this.appContext = appContext;
        msgPusherStore = appContext.getMsgPusherStore();
        convertMapStore = appContext.getConvertMapStore();
        syncHandler = appContext.getSyncHandler();

        MsgPusherClassPathRoot = new File(this.getClass().getResource("/").getPath()).getPath();
        Pattern pattern = Pattern.compile("file:(/.*)/(.*?jar)");
        Matcher match = pattern.matcher(MsgPusherClassPathRoot);
        if(match.find()) {
            MsgPusherClassPathRoot = match.group(1);
        }
        MsgPusherClassPathRoot += File.separator + "lib" + File.separator + "ext";

    }

    public void start() {

        List<MsgPusherPO> msgPushers = msgPusherStore.getAllMsgPusher();
        for(MsgPusherPO msgPusher : msgPushers) {
            PusherLoader pusherLoader = buildClassLoader(msgPusher);
            if(pusherLoader !=null) {
                msgPusherMap.put(msgPusher.getId(), pusherLoader);
            }
        }

        List<ConvertMapPO> convertMaps = convertMapStore.getAllConvertMap();
        for(ConvertMapPO convertMap : convertMaps) {
            ConvertMapper convertMapper = buildConvertMapper(convertMap);
            if(convertMapper!=null) {
                convertMapCache.put(convertMap.getId(), convertMapper);
            }
        }

        syncHandler.addListener(new SyncListener() {
            @Override
            public void onSyncEvent(SyncEvent syncEvent) {
                logger.info("tip=sync message config|"+syncEvent);
                flush(syncEvent);
            }
        });

    }

    public Class<RpcPusher> getMsgPusherClass(Integer msgPusherId) {

        PusherLoader pusherLoader = msgPusherMap.get(msgPusherId);
        try {
            if(pusherLoader !=null) {
                Class<RpcPusher>  msgPusherClass = (Class<RpcPusher>) pusherLoader.getClassLoader().loadClass(pusherLoader.getTargetClass());
                return msgPusherClass;
            }
        } catch (ClassNotFoundException e) {
            logger.error("tip=The Message Pusher Class is not exist|"+pusherLoader+"|exception=,"+e);
        }

        return null;
    }

    private PusherLoader buildClassLoader(MsgPusherPO msgPusher) {

        try {
            msgPusher = msgPusherStore.getMsgPusherById(msgPusher.getId());  //获取jar
            if(msgPusher == null || msgPusher.getJar() == null) {
                return null;
            }

            RpcClassLoader rpcClassLoader = new RpcClassLoader();
            rpcClassLoader.build(msgPusher.getJar(), MsgPusherClassPathRoot+File.separator+msgPusher.getId());

            String targetClass = IOUtils.toString(rpcClassLoader.getResourceAsStream("MainClass"), "utf-8");
            PusherLoader pusherLoader = new PusherLoader();
            pusherLoader.setClassLoader(rpcClassLoader);
            pusherLoader.setTargetClass(targetClass);
            pusherLoader.setVersion(msgPusher.getVersion());

            Class<RpcPusher>  msgPusherClass = (Class<RpcPusher>) pusherLoader.getClassLoader().loadClass(pusherLoader.getTargetClass());
            System.out.println("load class from msgPushJar: "+msgPusherClass);
            return pusherLoader;

        } catch (Exception e) {
            logger.error("tip=The Message Pusher ClassLoader build failed|"+msgPusher+"|exception=", e);
        }

        return null;

    }

    public void flush(SyncEvent syncEvent) {
        switch (syncEvent.getObjectType()) {
            case MSG_PUSHER:
                flushMsgPusher(syncEvent.getEventType(), syncEvent.getAddition());
                break;
            case MSG_CONVERTER:
                flushMsgConverter(syncEvent.getEventType(), syncEvent.getAddition());
                break;
            case CONFIG:
                changeConfig(syncEvent.getEventType(), syncEvent.getAddition());
                break;
        }
    }

    public void changeConfig(SyncEvent.EventType eventType, String addition) {
        Map<String, Object> paramsMap = JSON.parseObject(addition, HashMap.class);
        Integer fastWorkThreads = Integer.valueOf((String) paramsMap.get("fastWorkThreads"));
        if (fastWorkThreads != null) {
            appContext.getRunnerPool().setWorkThread(fastWorkThreads, RunnerPool.ThreadPoolType.Fast);
        }
        Integer slowWorkThreads = Integer.valueOf((String) paramsMap.get("slowWorkThreads"));
        if (slowWorkThreads != null) {
            appContext.getRunnerPool().setWorkThread(slowWorkThreads, RunnerPool.ThreadPoolType.Slow);
        }
    }

    public void flushMsgPusher(SyncEvent.EventType eventType, String addition) {

        MsgPusherPO newMsgPusher = JSON.parseObject(addition, MsgPusherPO.class);

        synchronized (String.valueOf(newMsgPusher.getId()).intern()) {
            switch (eventType) {
                case Add:
                    if(!deleteMsgPusherSet.contains(newMsgPusher.getId()) && !msgPusherMap.containsKey(newMsgPusher.getId())) {
                        PusherLoader pusherLoader = buildClassLoader(newMsgPusher);
                        if(pusherLoader !=null) {
                            msgPusherMap.put(newMsgPusher.getId(), pusherLoader);
                        }
                    }
                    break;
                case Update:
                    if(!deleteMsgPusherSet.contains(newMsgPusher.getId())) {
                        PusherLoader oldMsgPusher = msgPusherMap.get(newMsgPusher.getId());
                        if(oldMsgPusher == null || (newMsgPusher.getVersion() >= oldMsgPusher.getVersion())) {
                            PusherLoader pusherLoader = buildClassLoader(newMsgPusher);
                            if(pusherLoader !=null) {
                                msgPusherMap.put(newMsgPusher.getId(), pusherLoader);
                            }
                        }
                    }
                    break;
                case Delete:
                    msgPusherMap.remove(newMsgPusher.getId());
                    deleteMsgPusherSet.add(newMsgPusher.getId());
                    break;
            }
        }

    }

    private void flushMsgConverter(SyncEvent.EventType eventType, String addition) {

        ConvertMapPO newConvertMap = JSON.parseObject(addition, ConvertMapPO.class);

        synchronized (ConvertMapPO.class.getSimpleName()+String.valueOf(newConvertMap.getId())) {
            switch (eventType) {
                case Add:
                    if(!deleteConvertMapSet.contains(newConvertMap.getId())
                            && !convertMapCache.containsKey(newConvertMap.getId())) {
                        ConvertMapper convertMapper = buildConvertMapper(newConvertMap);
                        if(convertMapper!=null) {
                            convertMapCache.put(newConvertMap.getId(), convertMapper);
                        }
                    }
                    break;
                case Update:
                    if(!deleteConvertMapSet.contains(newConvertMap.getId())) {
                        ConvertMapper oldConvertMap = convertMapCache.get(newConvertMap.getId());
                        if(oldConvertMap==null || (newConvertMap.getVersion() >= oldConvertMap.getVersion())) {
                            ConvertMapper convertMapper = buildConvertMapper(newConvertMap);
                            if(convertMapper!=null) {
                                convertMapCache.put(newConvertMap.getId(), convertMapper);
                            }
                        }
                    }
                    break;
                case Delete:
                    convertMapCache.remove(newConvertMap.getId());
                    deleteConvertMapSet.add(newConvertMap.getId());
                    break;
            }
        }


    }

    private ConvertMapper buildConvertMapper(ConvertMapPO convertMap) {

        //缺省为版本V1，兼容历史配置
        int version = convertMap.getGeneration() == null ? ConvertVersion.V1.ordinal() : convertMap.getGeneration();

        try {
            if(ConvertVersion.V1.getValue() == version) {
                DataMap requestMap = RequestMapBuilder.build(convertMap.getRequestMap());
                ResponseMap responseMap = null;
                if(StringUtils.isNotEmpty(convertMap.getResponseMap())) {
                    responseMap = JAXBUtils.deserialize(new StringReader(convertMap.getResponseMap()), ResponseMap.class);
                }
                return new ConvertMapper(requestMap, responseMap, ConvertVersion.V1, convertMap.getVersion());
            } else if(ConvertVersion.V2.getValue() == version) {
                ResponseMap responseMap = null;
                if(StringUtils.isNotEmpty(convertMap.getResponseMap())) {
                    responseMap = JAXBUtils.deserialize(new StringReader(convertMap.getResponseMap()), ResponseMap.class);
                }
                return new ConvertMapper(convertMap.getRequestMap(), responseMap, ConvertVersion.V2, convertMap.getVersion(), convertMap.getHttpHeader());
            }
        } catch (JAXBException e) {
            logger.error("tip=The Converter Mapper failed init|convertMap="+convertMap+"|exception=", e);
            return null;
        }

        return null;

    }

    public ConvertMapper getConvertMapper(Integer convertMapId) {
        return convertMapCache.get(convertMapId);
    }

}

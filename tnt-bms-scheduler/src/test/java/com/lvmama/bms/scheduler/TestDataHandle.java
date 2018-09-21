package com.lvmama.bms.scheduler;

import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.cluster.sync.SyncHandler;
import com.lvmama.bms.core.domain.Message;
import com.lvmama.bms.core.domain.enums.MsgStatus;
import com.lvmama.bms.core.spi.ServiceLoader;
import com.lvmama.bms.scheduler.config.CfgException;
import com.lvmama.bms.scheduler.config.MsgSchedulerCfg;
import com.lvmama.bms.scheduler.config.MsgSchedulerCfgLoader;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.store.MessageStore;
import com.lvmama.bms.scheduler.store.MsgPushFailStore;
import com.lvmama.bms.scheduler.store.MsgTokenStore;
import com.lvmama.bms.scheduler.store.domain.po.MessagePO;
import com.lvmama.bms.scheduler.store.domain.po.MsgPushFailPO;
import com.lvmama.bms.scheduler.store.domain.po.MsgTokenPO;
import com.lvmama.bms.scheduler.store.factory.MessageStoreFactory;
import com.lvmama.bms.scheduler.support.MessageCleaner;
import com.lvmama.bms.scheduler.support.MessageCorrector;
import com.lvmama.bms.scheduler.support.MsgCacheManager;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class TestDataHandle {

    private static MsgSchedulerAppContext appContext;

    @BeforeClass
    public static void setUp() throws CfgException {

        appContext = new MsgSchedulerAppContext();

        MsgSchedulerCfgLoader msgSchedulerCfgLoader = new MsgSchedulerCfgLoader();
        MsgSchedulerCfg msgSchedulerCfg = msgSchedulerCfgLoader.load(new String[] {"--spring.profiles.active=dev"});

        Config config = new Config();
        for (Map.Entry<String, String> item : msgSchedulerCfg.getConfigs().entrySet()) {
            config.setParameter(item.getKey(), item.getValue());
        }

        appContext.setConfig(config);

        MessageStoreFactory messageStoreFactory = ServiceLoader.load(MessageStoreFactory.class, "");
        appContext.setMessageStore(messageStoreFactory.getMessageStore());
        appContext.setMsgTokenStore(messageStoreFactory.getMsgTokenStore());
        appContext.setTokenStore(messageStoreFactory.getTokenStore());
        appContext.setMsgTypeStore(messageStoreFactory.getMsgTypeStore());
        appContext.setGlobalStatisStore(messageStoreFactory.getGlobalStatisStore());
        appContext.setTokenStatisStore(messageStoreFactory.getTokenStatisStore());
        appContext.setMsgTypeStatisStore(messageStoreFactory.getMsgTypeStatisStore());
        appContext.setMsgTokenStatisStore(messageStoreFactory.getMsgTokenStatisStore());
        appContext.setMsgPushFailStore(messageStoreFactory.getMsgPushFailStore());

        SyncHandler syncHandler = new SyncHandler(appContext.getConfig());
        appContext.setSyncHandler(syncHandler);

        MsgCacheManager msgCacheManager = new MsgCacheManager(appContext);
        appContext.setMsgCacheManager(msgCacheManager);
        msgCacheManager.start();

    }

    @Test
    public void deleteExipredPushedMsg() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        MessageStore messageStore = appContext.getMessageStore();
        MsgTokenStore msgTokenStore = appContext.getMsgTokenStore();

        MessagePO message = new MessagePO();
        message.setMsgId("Test1");
        message.setMsgTypeId(109);
        message.setMsgContent("{\"name\":\"lisi\",\"age\":20}");
        message.setMsgStatus(MsgStatus.RECEIVE.ordinal());
        messageStore.saveMessage(message);

        message = new MessagePO();
        message.setMsgId("Test2");
        message.setMsgTypeId(109);
        message.setMsgContent("{\"name\":\"lisi\",\"age\":20}");
        message.setMsgStatus(MsgStatus.SENDING.ordinal());
        messageStore.saveMessage(message);

        message = new MessagePO();
        message.setMsgId("Test3");
        message.setMsgTypeId(109);
        message.setMsgContent("{\"name\":\"lisi\",\"age\":20}");
        message.setMsgStatus(MsgStatus.SENDING.ordinal());
        messageStore.saveMessage(message);
        msgTokenStore.saveMsgToken(message.getId(), new Integer[]{143205}, message.getMsgTypeId());

        message = new MessagePO();
        message.setMsgId("Test4");
        message.setMsgTypeId(109);
        message.setMsgContent("{\"name\":\"lisi\",\"age\":20}");
        message.setMsgStatus(MsgStatus.INVALID.ordinal());
        messageStore.saveMessage(message);
        msgTokenStore.saveMsgToken(message.getId(), new Integer[]{143205}, message.getMsgTypeId());

        Method deleteExipredPushedMsg = MessageCleaner.class.getDeclaredMethod("deleteExipredPushedMsg");
        deleteExipredPushedMsg.setAccessible(true);
        deleteExipredPushedMsg.invoke(new MessageCleaner(appContext));

    }

    @Test
    public void deleteExpiredFailedMsg() throws Exception {

        MsgPushFailStore msgPushFailStore = appContext.getMsgPushFailStore();
        MsgPushFailPO msgPushFailPO = new MsgPushFailPO();
        msgPushFailPO.setMsgId("Test911");
        msgPushFailPO.setTokenId(143205);
        msgPushFailPO.setMsgTypeId(109);
        msgPushFailPO.setPushTime(new Date());
        msgPushFailStore.save(msgPushFailPO);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        msgPushFailPO.setPushTime(calendar.getTime());
        msgPushFailStore.save(msgPushFailPO);

        calendar.add(Calendar.DAY_OF_MONTH, -5);
        msgPushFailPO.setPushTime(calendar.getTime());
        msgPushFailStore.save(msgPushFailPO);

        Method deleteExpiredFailedMsg = MessageCleaner.class.getDeclaredMethod("deleteExpiredFailedMsg");
        deleteExpiredFailedMsg.setAccessible(true);
        deleteExpiredFailedMsg.invoke(new MessageCleaner(appContext));

    }

    @Test
    public void correctMsgToken() throws Exception {

        MessageStore messageStore = appContext.getMessageStore();
        MsgTokenStore msgTokenStore = appContext.getMsgTokenStore();

        MessagePO message = new MessagePO();
        message.setMsgId("Test3");
        message.setMsgTypeId(109);
        message.setMsgContent("{\"name\":\"lisi\",\"age\":20}");
        message.setMsgStatus(MsgStatus.SENDING.ordinal());

//        messageStore.saveMessage(message);
//        msgTokenStore.saveMsgToken(message.getId(), new Integer[]{143205}, message.getMsgTypeId());
//
//        messageStore.saveMessage(message);
//        msgTokenStore.saveMsgToken(message.getId(), new Integer[]{143205}, message.getMsgTypeId());

        Method recoverDeathMsgToken = MessageCorrector.class.getDeclaredMethod("recoverDeathMsgToken");
        recoverDeathMsgToken.setAccessible(true);
        recoverDeathMsgToken.invoke(new MessageCorrector(appContext));

    }









}

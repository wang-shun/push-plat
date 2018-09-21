package com.lvmama.bms.scheduler;

import com.lvmama.bms.alarm.email.EmailAlarmNotifier;
import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.core.cluster.AbstractServerNode;
import com.lvmama.bms.core.cluster.sync.SyncHandler;
import com.lvmama.bms.core.spi.ServiceLoader;
import com.lvmama.bms.lock.RedisDistributedLock;
import com.lvmama.bms.remoting.RemotingProcessor;
import com.lvmama.bms.scheduler.channel.ChannelManager;
import com.lvmama.bms.scheduler.cmd.AddJobHttpCmd;
import com.lvmama.bms.scheduler.cmd.LoadJobHttpCmd;
import com.lvmama.bms.scheduler.cmd.TriggerJobManuallyHttpCmd;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.domain.MsgSchedulerNode;
import com.lvmama.bms.scheduler.monitor.MsgCounter;
import com.lvmama.bms.scheduler.monitor.MsgSchedulerMStatReporter;
import com.lvmama.bms.scheduler.monitor.MsgStatis;
import com.lvmama.bms.scheduler.processor.RemotingDispatcher;
import com.lvmama.bms.scheduler.store.factory.MessageStoreFactory;
import com.lvmama.bms.scheduler.support.*;
import com.lvmama.bms.scheduler.support.assign.TaskAssigner;
import com.lvmama.bms.scheduler.support.cluster.MsgClientManager;
import com.lvmama.bms.scheduler.support.cluster.MsgPusherManager;
import com.lvmama.bms.scheduler.support.listener.MsgNodeChangeListener;
import com.lvmama.bms.scheduler.support.listener.MsgSchedulerMasterChangeListener;
import com.lvmama.bms.scheduler.support.listener.TaskAssignChangeListener;

/**
 * @author Robert HG (254963746@qq.com) on 7/23/14.
 */
public class MessageScheduler extends AbstractServerNode<MsgSchedulerNode, MsgSchedulerAppContext> {

    public MessageScheduler() {
        // 添加节点变化监听器
        addNodeChangeListener(new MsgNodeChangeListener(appContext));
        // 添加master节点变化监听器
        addMasterChangeListener(new MsgSchedulerMasterChangeListener(appContext));
    }

    @Override
    protected void beforeStart() throws Exception {
        // 监控中心
        appContext.setMStatReporter(new MsgSchedulerMStatReporter(appContext));
        // channel 管理者
        appContext.setChannelManager(new ChannelManager());
        // JobClient 管理者
        appContext.setMsgClientManager(new MsgClientManager(appContext));
        // TaskTracker 管理者
        appContext.setMsgPusherManager(new MsgPusherManager(appContext));

        // injectRemotingServer
        appContext.setRemotingServer(remotingServer);

        appContext.getHttpCmdServer().registerCommands(
                new LoadJobHttpCmd(appContext),     // 手动加载任务
                new AddJobHttpCmd(appContext),
                new TriggerJobManuallyHttpCmd(appContext));     // 添加任务


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

        appContext.setRedisTemplate(new RedisTemplate(appContext.getConfig()));
        appContext.setRedisDistributedLock(new RedisDistributedLock(appContext.getRedisTemplate()));

        appContext.setTaskAssigner(new TaskAssigner(appContext, node.getIdentity()));

        addNodeChangeListener(new TaskAssignChangeListener(appContext)); //节点任务分配

        MsgCacheManager msgCacheManager = new MsgCacheManager(appContext);
        appContext.setMsgCacheManager(msgCacheManager);
        msgCacheManager.start();

        syncHandler.start();

        appContext.setAlarmNotifier(new EmailAlarmNotifier(appContext));
        appContext.setMsgStatis(new MsgStatis(appContext));
        appContext.setMsgCounter(new MsgCounter(appContext));

        appContext.setMessageReceiver(new MessageReceiver(appContext));
        appContext.setMessageDispatcher(new MessageDispatcher(appContext));

        //错误消息修复器
        appContext.setMessageCorrector(new MessageCorrector(appContext));

        appContext.setMessageCleaner(new MessageCleaner(appContext));

        MsgManualSend msgManualSend = new MsgManualSend(appContext);
        appContext.setMsgManualSend(msgManualSend);
        msgManualSend.start();

        appContext.getMessageDispatcher().start();

    }

    @Override
    protected void afterStart() {
        appContext.getChannelManager().start();
        appContext.getMStatReporter().start();
    }

    @Override
    protected void afterStop() {
        appContext.getChannelManager().stop();
        appContext.getMStatReporter().stop();
        appContext.getHttpCmdServer().stop();
    }

    @Override
    protected void beforeStop() {
    }

    @Override
    protected RemotingProcessor getDefaultProcessor() {
        return new RemotingDispatcher(appContext);
    }

}

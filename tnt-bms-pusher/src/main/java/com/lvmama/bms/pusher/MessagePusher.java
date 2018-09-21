package com.lvmama.bms.pusher;

import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.core.cluster.AbstractClientNode;
import com.lvmama.bms.core.cluster.sync.SyncHandler;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.constant.Level;
import com.lvmama.bms.pusher.cmd.JobTerminateCmd;
import com.lvmama.bms.pusher.domain.MsgPusherAppContext;
import com.lvmama.bms.pusher.domain.MsgPusherNode;
import com.lvmama.bms.pusher.monitor.MsgPusherMStatReporter;
import com.lvmama.bms.pusher.monitor.StopWorkingMonitor;
import com.lvmama.bms.pusher.processor.RemotingDispatcher;
import com.lvmama.bms.pusher.protocol.ProtocolPusherFactory;
import com.lvmama.bms.pusher.runner.RunnerPool;
import com.lvmama.bms.pusher.store.factory.MessageStoreFactory;
import com.lvmama.bms.pusher.store.factory.impl.MySqlMessageStoreFactory;
import com.lvmama.bms.pusher.support.MsgCacheManager;
import com.lvmama.bms.pusher.support.MsgPullMachine;
import com.lvmama.bms.remoting.RemotingProcessor;

import java.util.Map;

/**
 * @author Robert HG (254963746@qq.com) on 8/14/14.
 *         任务执行节点
 */
public class MessagePusher extends AbstractClientNode<MsgPusherNode, MsgPusherAppContext> {

    @Override
    protected void beforeStart() throws Exception {

        appContext.setRedisTemplate(new RedisTemplate(appContext.getConfig()));

        MessageStoreFactory messageStoreFactory = new MySqlMessageStoreFactory();
        appContext.setMsgPusherStore(messageStoreFactory.getMsgPushStore());
        appContext.setConvertMapStore(messageStoreFactory.getConvertMapperStore());

        SyncHandler syncHandler = new SyncHandler(appContext.getConfig());
        appContext.setSyncHandler(syncHandler);

        MsgCacheManager msgCacheManager = new MsgCacheManager(appContext);
        appContext.setMsgCacheManager(msgCacheManager);
        msgCacheManager.start();

        syncHandler.start();

        appContext.setProtocolPusherFactory(new ProtocolPusherFactory(appContext));

        appContext.setMStatReporter(new MsgPusherMStatReporter(appContext));

        // 设置 线程池
        appContext.setRemotingClient(remotingClient);
        appContext.setRunnerPool(new RunnerPool(appContext));
        appContext.getMStatReporter().start();
        appContext.setMsgPullMachine(new MsgPullMachine(appContext));
        appContext.setStopWorkingMonitor(new StopWorkingMonitor(appContext));

        appContext.getHttpCmdServer().registerCommands(
                new JobTerminateCmd(appContext));     // 终止某个正在执行的任务


    }

    @Override
    protected void afterStart() {
        if (config.getParameter(ExtConfig.TASK_TRACKER_STOP_WORKING_ENABLE, false)) {
            appContext.getStopWorkingMonitor().start();
        }
    }

    @Override
    protected void afterStop() {
        appContext.getMStatReporter().stop();
        appContext.getStopWorkingMonitor().stop();
        appContext.getRunnerPool().shutDown();
    }

    @Override
    protected void beforeStop() {
    }

    @Override
    protected RemotingProcessor getDefaultProcessor() {
        return new RemotingDispatcher(appContext);
    }

    /**
     * 设置业务日志记录级别
     */
    public void setBizLoggerLevel(Level level) {
        if (level != null) {
            appContext.setBizLogLevel(level);
        }
    }

    public void setPullRate(int rate) {
    	Map<String, String> parameter = config.getParameters();
    	parameter.put(ExtConfig.JOB_PULL_FREQUENCY, String.valueOf(rate));
    }

    public void setFastWorkThreads(int workThreads) {
        config.setFastWorkThreads(workThreads);
    }

    public void setSlowWorkThreads(int workThreads) {
        config.setSlowWorkThreads(workThreads);
    }


}

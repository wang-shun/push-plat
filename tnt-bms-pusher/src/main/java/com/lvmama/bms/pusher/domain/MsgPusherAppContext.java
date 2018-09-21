package com.lvmama.bms.pusher.domain;

import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.core.cluster.sync.SyncHandler;
import com.lvmama.bms.core.constant.Level;
import com.lvmama.bms.core.remoting.RemotingClientDelegate;
import com.lvmama.bms.pusher.monitor.StopWorkingMonitor;
import com.lvmama.bms.pusher.protocol.ProtocolPusherFactory;
import com.lvmama.bms.pusher.runner.RunnerPool;
import com.lvmama.bms.pusher.store.ConvertMapStore;
import com.lvmama.bms.pusher.store.MsgPusherStore;
import com.lvmama.bms.pusher.support.MsgCacheManager;
import com.lvmama.bms.pusher.support.MsgPullMachine;

/**
 * @author Robert HG (254963746@qq.com) on 3/30/15.
 */
public class MsgPusherAppContext extends AppContext {

    private RemotingClientDelegate remotingClient;
    // runner 线程池
    private RunnerPool runnerPool;

    // Pull Job Machine
    private MsgPullMachine msgPullMachine;

    private StopWorkingMonitor stopWorkingMonitor;
    /**
     * 业务日志记录级别
     */
    private Level bizLogLevel;
    /**
     * 执行任务的class
     */
    private Class<?> jobRunnerClass;

    private ProtocolPusherFactory protocolPusherFactory;

    private RedisTemplate redisTemplate;

    private MsgPusherStore msgPusherStore;

    private ConvertMapStore convertMapStore;

    private MsgCacheManager msgCacheManager;

    private SyncHandler syncHandler;

    public StopWorkingMonitor getStopWorkingMonitor() {
        return stopWorkingMonitor;
    }

    public void setStopWorkingMonitor(StopWorkingMonitor stopWorkingMonitor) {
        this.stopWorkingMonitor = stopWorkingMonitor;
    }

    public RunnerPool getRunnerPool() {
        return runnerPool;
    }

    public void setRunnerPool(RunnerPool runnerPool) {
        this.runnerPool = runnerPool;
    }

    public Level getBizLogLevel() {
        return bizLogLevel;
    }

    public void setBizLogLevel(Level bizLogLevel) {
        this.bizLogLevel = bizLogLevel;
    }

    public Class<?> getJobRunnerClass() {
        return jobRunnerClass;
    }

    public void setJobRunnerClass(Class<?> jobRunnerClass) {
        this.jobRunnerClass = jobRunnerClass;
    }

    public RemotingClientDelegate getRemotingClient() {
        return remotingClient;
    }

    public void setRemotingClient(RemotingClientDelegate remotingClient) {
        this.remotingClient = remotingClient;
    }

    public MsgPullMachine getMsgPullMachine() {
        return msgPullMachine;
    }

    public void setMsgPullMachine(MsgPullMachine msgPullMachine) {
        this.msgPullMachine = msgPullMachine;
    }

    public ProtocolPusherFactory getProtocolPusherFactory() {
        return protocolPusherFactory;
    }

    public void setProtocolPusherFactory(ProtocolPusherFactory protocolPusherFactory) {
        this.protocolPusherFactory = protocolPusherFactory;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public MsgPusherStore getMsgPusherStore() {
        return msgPusherStore;
    }

    public void setMsgPusherStore(MsgPusherStore msgPusherStore) {
        this.msgPusherStore = msgPusherStore;
    }

    public MsgCacheManager getMsgCacheManager() {
        return msgCacheManager;
    }

    public void setMsgCacheManager(MsgCacheManager msgCacheManager) {
        this.msgCacheManager = msgCacheManager;
    }

    public SyncHandler getSyncHandler() {
        return syncHandler;
    }

    public void setSyncHandler(SyncHandler syncHandler) {
        this.syncHandler = syncHandler;
    }

    public ConvertMapStore getConvertMapStore() {
        return convertMapStore;
    }

    public void setConvertMapStore(ConvertMapStore convertMapStore) {
        this.convertMapStore = convertMapStore;
    }
}

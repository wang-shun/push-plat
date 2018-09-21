package com.lvmama.bms.scheduler.domain;

import com.lvmama.bms.alarm.AlarmNotifier;
import com.lvmama.bms.cache.RedisTemplate;
import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.core.cluster.sync.SyncHandler;
import com.lvmama.bms.core.remoting.RemotingServerDelegate;
import com.lvmama.bms.lock.RedisDistributedLock;
import com.lvmama.bms.scheduler.channel.ChannelManager;
import com.lvmama.bms.scheduler.monitor.MsgCounter;
import com.lvmama.bms.scheduler.monitor.MsgStatis;
import com.lvmama.bms.scheduler.store.*;
import com.lvmama.bms.scheduler.support.*;
import com.lvmama.bms.scheduler.support.assign.TaskAssigner;
import com.lvmama.bms.scheduler.support.cluster.MsgClientManager;
import com.lvmama.bms.scheduler.support.cluster.MsgPusherManager;

/**
 * MessageScheduler Application
 *
 * @author Robert HG (254963746@qq.com) on 3/30/15.
 */
public class MsgSchedulerAppContext extends AppContext {

    private RemotingServerDelegate remotingServer;
    // channel manager
    private ChannelManager channelManager;
    // JobClient manager for job tracker
    private MsgClientManager msgClientManager;
    // TaskTracker manager for job tracker
    private MsgPusherManager msgPusherManager;

    private MessageStore messageStore;
    private MsgTokenStore msgTokenStore;
    private MsgTypeStore msgTypeStore;
    private TokenStore tokenStore;
    private GlobalStatisStore globalStatisStore;
    private TokenStatisStore tokenStatisStore;
    private MsgTypeStatisStore msgTypeStatisStore;
    private MsgTokenStatisStore msgTokenStatisStore;
    private MsgPushFailStore msgPushFailStore;

    private MsgStatis msgStatis;
    private MsgCounter msgCounter;
    private AlarmNotifier alarmNotifier;

    private MessageReceiver messageReceiver;
    private MsgCacheManager msgCacheManager;
    private MessageDispatcher messageDispatcher;
    private RedisTemplate redisTemplate;
    private SyncHandler syncHandler;
    private MsgManualSend msgManualSend;
    private volatile boolean isMaster;

    private RedisDistributedLock redisDistributedLock;

    private MessageCorrector messageCorrector;

    private MessageCleaner messageCleaner;

    private TaskAssigner taskAssigner;

    public MessageCorrector getMessageCorrector() {
        return messageCorrector;
    }

    public void setMessageCorrector(MessageCorrector messageCorrector) {
        this.messageCorrector = messageCorrector;
    }

    public MessageCleaner getMessageCleaner() {
        return messageCleaner;
    }

    public void setMessageCleaner(MessageCleaner messageCleaner) {
        this.messageCleaner = messageCleaner;
    }

    public RedisDistributedLock getRedisDistributedLock() {
        return redisDistributedLock;
    }

    public void setRedisDistributedLock(RedisDistributedLock redisDistributedLock) {
        this.redisDistributedLock = redisDistributedLock;
    }

    public RemotingServerDelegate getRemotingServer() {
        return remotingServer;
    }

    public void setRemotingServer(RemotingServerDelegate remotingServer) {
        this.remotingServer = remotingServer;
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    public void setChannelManager(ChannelManager channelManager) {
        this.channelManager = channelManager;
    }

    public MsgClientManager getMsgClientManager() {
        return msgClientManager;
    }

    public void setMsgClientManager(MsgClientManager msgClientManager) {
        this.msgClientManager = msgClientManager;
    }

    public MsgPusherManager getMsgPusherManager() {
        return msgPusherManager;
    }

    public void setMsgPusherManager(MsgPusherManager msgPusherManager) {
        this.msgPusherManager = msgPusherManager;
    }

    public MessageStore getMessageStore() {
        return messageStore;
    }

    public void setMessageStore(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    public MsgTokenStore getMsgTokenStore() {
        return msgTokenStore;
    }

    public void setMsgTokenStore(MsgTokenStore msgTokenStore) {
        this.msgTokenStore = msgTokenStore;
    }

    public MsgTypeStore getMsgTypeStore() {
        return msgTypeStore;
    }

    public void setMsgTypeStore(MsgTypeStore msgTypeStore) {
        this.msgTypeStore = msgTypeStore;
    }

    public TokenStore getTokenStore() {
        return tokenStore;
    }

    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public MessageReceiver getMessageReceiver() {
        return messageReceiver;
    }

    public void setMessageReceiver(MessageReceiver messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public MsgCacheManager getMsgCacheManager() {
        return msgCacheManager;
    }

    public void setMsgCacheManager(MsgCacheManager msgCacheManager) {
        this.msgCacheManager = msgCacheManager;
    }

    public MessageDispatcher getMessageDispatcher() {
        return messageDispatcher;
    }

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public SyncHandler getSyncHandler() {
        return syncHandler;
    }

    public void setSyncHandler(SyncHandler syncHandler) {
        this.syncHandler = syncHandler;
    }

    public GlobalStatisStore getGlobalStatisStore() {
        return globalStatisStore;
    }

    public void setGlobalStatisStore(GlobalStatisStore globalStatisStore) {
        this.globalStatisStore = globalStatisStore;
    }

    public TokenStatisStore getTokenStatisStore() {
        return tokenStatisStore;
    }

    public void setTokenStatisStore(TokenStatisStore tokenStatisStore) {
        this.tokenStatisStore = tokenStatisStore;
    }

    public MsgTypeStatisStore getMsgTypeStatisStore() {
        return msgTypeStatisStore;
    }

    public void setMsgTypeStatisStore(MsgTypeStatisStore msgTypeStatisStore) {
        this.msgTypeStatisStore = msgTypeStatisStore;
    }

    public MsgTokenStatisStore getMsgTokenStatisStore() {
        return msgTokenStatisStore;
    }

    public void setMsgTokenStatisStore(MsgTokenStatisStore msgTokenStatisStore) {
        this.msgTokenStatisStore = msgTokenStatisStore;
    }

    public MsgPushFailStore getMsgPushFailStore() {
        return msgPushFailStore;
    }

    public void setMsgPushFailStore(MsgPushFailStore msgPushFailStore) {
        this.msgPushFailStore = msgPushFailStore;
    }

    public MsgStatis getMsgStatis() {
        return msgStatis;
    }

    public void setMsgStatis(MsgStatis msgStatis) {
        this.msgStatis = msgStatis;
    }

    public MsgCounter getMsgCounter() {
        return msgCounter;
    }

    public void setMsgCounter(MsgCounter msgCounter) {
        this.msgCounter = msgCounter;
    }

    public AlarmNotifier getAlarmNotifier() {
        return alarmNotifier;
    }

    public void setAlarmNotifier(AlarmNotifier alarmNotifier) {
        this.alarmNotifier = alarmNotifier;
    }

    public MsgManualSend getMsgManualSend() {
        return msgManualSend;
    }

    public void setMsgManualSend(MsgManualSend msgManualSend) {
        this.msgManualSend = msgManualSend;
    }

    public TaskAssigner getTaskAssigner() {
        return taskAssigner;
    }

    public void setTaskAssigner(TaskAssigner taskAssigner) {
        this.taskAssigner = taskAssigner;
    }

}

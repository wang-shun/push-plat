package com.lvmama.bms.core.remoting;

import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.core.cluster.Node;
import com.lvmama.bms.core.cluster.NodeType;
import com.lvmama.bms.core.commons.utils.CollectionUtils;
import com.lvmama.bms.core.factory.NamedThreadFactory;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.protocol.MsgProtos;
import com.lvmama.bms.core.protocol.command.HeartBeatRequest;
import com.lvmama.bms.ec.EventInfo;
import com.lvmama.bms.ec.EventSubscriber;
import com.lvmama.bms.ec.Observer;
import com.lvmama.bms.remoting.protocol.RemotingCommand;
import com.lvmama.bms.core.constant.EcTopic;
import com.lvmama.bms.core.logger.Logger;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 如果用来发送心跳包，当没有连接上JobTracker的时候，启动快速检测连接；连接后，采用慢周期检测来保持长连接
 *
 * @author Robert HG (254963746@qq.com) on 7/25/14.
 */
public class HeartBeatMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeartBeatMonitor.class.getSimpleName());

    // 用来定时发送心跳
    private final ScheduledExecutorService PING_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(1, new NamedThreadFactory("LTS-HeartBeat-Ping", true));
    private ScheduledFuture<?> pingScheduledFuture;
    // 当没有可用的JobTracker的时候，启动这个来快速的检查（小间隔）
    private final ScheduledExecutorService FAST_PING_EXECUTOR = Executors.newScheduledThreadPool(1, new NamedThreadFactory("LTS-HeartBeat-Fast-Ping", true));
    private ScheduledFuture<?> fastPingScheduledFuture;

    private RemotingClientDelegate remotingClient;
    private AppContext appContext;
    private EventSubscriber jobTrackerUnavailableEventSubscriber;

    public HeartBeatMonitor(RemotingClientDelegate remotingClient, AppContext appContext) {
        this.remotingClient = remotingClient;
        this.appContext = appContext;
        this.jobTrackerUnavailableEventSubscriber = new EventSubscriber(HeartBeatMonitor.class.getName()
                + "_PING_" + appContext.getConfig().getIdentity(),
                new Observer() {
                    @Override
                    public void onObserved(EventInfo eventInfo) {
                        startFastPing();
                        stopPing();
                    }
                });
        appContext.getEventCenter().subscribe(new EventSubscriber(HeartBeatMonitor.class.getName()
                + "_NODE_ADD_" + appContext.getConfig().getIdentity(), new Observer() {
            @Override
            public void onObserved(EventInfo eventInfo) {
                Node node = (Node) eventInfo.getParam("node");
                if (node == null || NodeType.MSG_SCHEDULER != node.getNodeType()) {
                    return;
                }
                try {
                    check(node);
                } catch (Throwable ignore) {
                }
            }
        }), EcTopic.NODE_ADD);
    }

    private AtomicBoolean pingStart = new AtomicBoolean(false);
    private AtomicBoolean fastPingStart = new AtomicBoolean(false);

    public void start() {
        startFastPing();
    }

    public void stop() {
        stopPing();
        stopFastPing();
    }

    private void startPing() {
        try {
            if (pingStart.compareAndSet(false, true)) {
                // 用来监听 JobTracker不可用的消息，然后马上启动 快速检查定时器
                appContext.getEventCenter().subscribe(jobTrackerUnavailableEventSubscriber, EcTopic.NO_JOB_TRACKER_AVAILABLE);
                if (pingScheduledFuture == null) {
                    pingScheduledFuture = PING_EXECUTOR_SERVICE.scheduleWithFixedDelay(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (pingStart.get()) {
                                        ping();
                                    }
                                }
                            }, 30, 30, TimeUnit.SECONDS);      // 30s 一次心跳
                }
                LOGGER.debug("Start slow ping success.");
            }
        } catch (Throwable t) {
            LOGGER.error("Start slow ping failed.", t);
        }
    }

    private void stopPing() {
        try {
            if (pingStart.compareAndSet(true, false)) {
//                pingScheduledFuture.cancel(true);
//                PING_EXECUTOR_SERVICE.shutdown();
                appContext.getEventCenter().unSubscribe(EcTopic.NO_JOB_TRACKER_AVAILABLE, jobTrackerUnavailableEventSubscriber);
                LOGGER.debug("Stop slow ping success.");
            }
        } catch (Throwable t) {
            LOGGER.error("Stop slow ping failed.", t);
        }
    }

    private void startFastPing() {
        if (fastPingStart.compareAndSet(false, true)) {
            try {
                // 2s 一次进行检查
                if (fastPingScheduledFuture == null) {
                    fastPingScheduledFuture = FAST_PING_EXECUTOR.scheduleWithFixedDelay(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (fastPingStart.get()) {
                                        ping();
                                    }
                                }
                            }, 500, 500, TimeUnit.MILLISECONDS);
                }
                LOGGER.debug("Start fast ping success.");
            } catch (Throwable t) {
                LOGGER.error("Start fast ping failed.", t);
            }
        }
    }

    private void stopFastPing() {
        try {
            if (fastPingStart.compareAndSet(true, false)) {
//                fastPingScheduledFuture.cancel(true);
//                FAST_PING_EXECUTOR.shutdown();
                LOGGER.debug("Stop fast ping success.");
            }
        } catch (Throwable t) {
            LOGGER.error("Stop fast ping failed.", t);
        }
    }

    private AtomicBoolean running = new AtomicBoolean(false);

    private void ping() {
        try {
            if (running.compareAndSet(false, true)) {
                // to ensure only one thread go there
                try {
                    check();
                } finally {
                    running.compareAndSet(true, false);
                }
            }
        } catch (Throwable t) {
            LOGGER.error("Ping JobTracker error", t);
        }
    }

    private void check() {
        List<Node> jobTrackers = appContext.getSubscribedNodeManager().getNodeList(NodeType.MSG_SCHEDULER);
        if (CollectionUtils.isEmpty(jobTrackers)) {
            return;
        }
        for (Node jobTracker : jobTrackers) {
            check(jobTracker);
        }
    }

    private void check(Node jobTracker) {
        // 每个JobTracker 都要发送心跳
        if (beat(remotingClient, jobTracker.getAddress())) {
            remotingClient.addJobTracker(jobTracker);
            if (!remotingClient.isServerEnable()) {
                remotingClient.setServerEnable(true);
                appContext.getEventCenter().publishAsync(new EventInfo(EcTopic.JOB_TRACKER_AVAILABLE));
            } else {
                remotingClient.setServerEnable(true);
            }
            stopFastPing();
            startPing();
        } else {
            remotingClient.removeJobTracker(jobTracker);
        }
    }

    /**
     * 发送心跳
     */
    private boolean beat(RemotingClientDelegate remotingClient, String addr) {

        HeartBeatRequest commandBody = appContext.getCommandBodyWrapper().wrapper(new HeartBeatRequest());

        RemotingCommand request = RemotingCommand.createRequestCommand(
                MsgProtos.RequestCode.HEART_BEAT.code(), commandBody);
        try {
            RemotingCommand response = remotingClient.getRemotingClient().invokeSync(addr, request, 5000);
            if (response != null && MsgProtos.ResponseCode.HEART_BEAT_SUCCESS ==
                    MsgProtos.ResponseCode.valueOf(response.getCode())) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("heart beat success. ");
                }
                return true;
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
        return false;
    }

}

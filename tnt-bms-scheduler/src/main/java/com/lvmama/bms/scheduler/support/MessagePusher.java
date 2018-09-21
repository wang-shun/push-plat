package com.lvmama.bms.scheduler.support;


import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.core.commons.utils.Holder;
import com.lvmama.bms.core.constant.Constants;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.domain.dto.MessageDTO;
import com.lvmama.bms.core.domain.enums.MsgStatus;
import com.lvmama.bms.core.exception.RemotingSendException;
import com.lvmama.bms.core.factory.NamedThreadFactory;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.protocol.MsgProtos;
import com.lvmama.bms.core.protocol.command.MsgPullRequest;
import com.lvmama.bms.core.protocol.command.MsgPushRequest;
import com.lvmama.bms.core.registry.Registry;
import com.lvmama.bms.core.remoting.RemotingServerDelegate;
import com.lvmama.bms.remoting.AsyncCallback;
import com.lvmama.bms.remoting.ResponseFuture;
import com.lvmama.bms.remoting.protocol.RemotingCommand;
import com.lvmama.bms.scheduler.domain.MsgPusherNode;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.store.MsgTokenStore;
import com.lvmama.bms.zookeeper.DataListener;

import java.util.concurrent.*;

/**
 * @author Robert HG (254963746@qq.com) on 8/18/14.
 *         任务分发管理
 */
public class MessagePusher {

    private final Logger LOGGER = LoggerFactory.getLogger(MessagePusher.class);
    private MsgSchedulerAppContext appContext;
    private final ExecutorService executorService;
    private RemotingServerDelegate remotingServer;
    private MessageDispatcher messageDispatcher;
    private MsgTokenStore msgTokenStore;

    public MessagePusher(MsgSchedulerAppContext appContext) {
        this.appContext = appContext;
        this.remotingServer = appContext.getRemotingServer();
        this.messageDispatcher = appContext.getMessageDispatcher();
        this.executorService = getPushExecutor(appContext);
        this.msgTokenStore = appContext.getMsgTokenStore();
    }

    public void concurrentPush(final MsgPullRequest request) {
    	
        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    push(request);
                } catch (Exception e) {
                    LOGGER.error("tip=Job push failed!|exception=", e);
                }
            }
        });
    }

    private void push(final MsgPullRequest request) {

        String nodeGroup = request.getNodeGroup();
        String identity = request.getIdentity();

        appContext.getMsgPusherManager().updateTaskTrackerAvailableThreads(nodeGroup,
                identity, request.getFastIdleThreads(), request.getSlowIdleThreads(), request.getTimestamp());

        MsgPusherNode msgPusherNode = appContext.getMsgPusherManager().getTaskTrackerNode(nodeGroup, identity);

        if (msgPusherNode == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("tip=taskTrackerNodeGroup:{}, taskTrackerIdentity:{} , didn't have node.", nodeGroup, identity);
            }
            return;
        }

        LOGGER.info("tip={} pull message|fastIdleThreads={}|slowIdleThreads={}", msgPusherNode, msgPusherNode.getFastIdleThread().getAndAdd(0),
                msgPusherNode.getSlowIdleThread().getAndAdd(0));

        int fastIdleThreads = msgPusherNode.getFastIdleThread().get();
        int slowIdleThreads = msgPusherNode.getSlowIdleThread().get();

        boolean isEnableFastSend = true;
        boolean isEnableSlowSend = true;

        while(true) {

            if(isEnableFastSend && fastIdleThreads > 0) {

                MsgPushResult result = send(remotingServer, msgPusherNode, MessageDispatcher.MessageSpeed.Fast);
                switch (result) {
                    case SUCCESS:
                        fastIdleThreads = msgPusherNode.getFastIdleThread().decrementAndGet();
                        break;
                    case FAILED:
                        // 还是要继续发送
                        break;
                    case NO_MSG:
                        // 没有任务了
                        isEnableFastSend = false;
                        break;
                    case SENT_ERROR:
                        // MsgPusher链接失败
                        isEnableFastSend = false;
                        isEnableSlowSend = false;
                        break;
                }

            }

            if(isEnableSlowSend && slowIdleThreads > 0) {

                MsgPushResult result = send(remotingServer, msgPusherNode, MessageDispatcher.MessageSpeed.Slow);
                switch (result) {
                    case SUCCESS:
                        slowIdleThreads = msgPusherNode.getSlowIdleThread().decrementAndGet();
                        break;
                    case FAILED:
                        // 还是要继续发送
                        break;
                    case NO_MSG:
                        // 没有任务了
                        isEnableSlowSend = false;
                        break;
                    case SENT_ERROR:
                        // MsgPusher链接失败
                        isEnableFastSend = false;
                        isEnableSlowSend = false;
                        break;
                }
            }

            if(!(isEnableFastSend && fastIdleThreads > 0)
                    && !(isEnableSlowSend && slowIdleThreads > 0)) {
                break;
            }

        }


    }

    /**
     * 是否推送成功
     */
    private MsgPushResult send(final RemotingServerDelegate remotingServer, final MsgPusherNode msgPusherNode, MessageDispatcher.MessageSpeed speed) {

        MsgPushRequest body = appContext.getCommandBodyWrapper().wrapper(new MsgPushRequest());
        final MessageDTO message = messageDispatcher.getNextMessage(speed);
        if(message == null) {
            return MsgPushResult.NO_MSG;
        }
        body.setMessage(message);

        RemotingCommand commandRequest = RemotingCommand.createRequestCommand(MsgProtos.RequestCode.PUSH_JOB.code(), body);

        final Holder<Boolean> pushSuccess = new Holder<Boolean>(false);

        final CountDownLatch latch = new CountDownLatch(1);
        try {
            LOGGER.info("tip=start push message to {}|{}", msgPusherNode.getIdentity(), message.clearMsgContent());
            remotingServer.invokeAsync(msgPusherNode.getChannel().getChannel(), commandRequest, new AsyncCallback() {
                @Override
                public void operationComplete(ResponseFuture responseFuture) {
                    try {
                        RemotingCommand responseCommand = responseFuture.getResponseCommand();
                        if (responseCommand == null) {
                            LOGGER.warn("tip=fail push message to {}, response command is null!|{}", msgPusherNode.getIdentity(), message);
                            return;
                        }
                        if (responseCommand.getCode() == MsgProtos.ResponseCode.JOB_PUSH_SUCCESS.code()) {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("tip=success push message to {}|{}", msgPusherNode.getIdentity(), message);
                            }
                            pushSuccess.set(true);
                        } else {
                            LOGGER.warn("tip=fail push message to {}|{}", msgPusherNode.getIdentity(), message);
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            });

        } catch (RemotingSendException e) {
            LOGGER.error("tip=remoting send error|"+message+"|exception=", e);
        }

        try {
            latch.await(Constants.LATCH_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("tip=push request interrupted|"+message+"|exception=", e);
        }

        if (!pushSuccess.get()) {
            msgTokenStore.updateStatus(message.getMsgTokenId(), MsgStatus.RECEIVE.ordinal(), message.getMsgTypeId()); //恢复
            return MsgPushResult.SENT_ERROR;
        }

        return MsgPushResult.SUCCESS;

    }
    
    public ExecutorService getPushExecutor(AppContext appContext) {
    	
    	Registry registry = appContext.getRegistry();
    	String path = registry.getAbsolutePath(appContext.getConfig(), ExtConfig.PUSH_THREAD);
    	final int pushExecutorSize = registry.getConfig(path, Constants.AVAILABLE_PROCESSOR * 5);
    	
    	final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(pushExecutorSize,
                new NamedThreadFactory(MessagePusher.class.getSimpleName(), true));
    	
    	registry.addListener(path, new DataListener() {
			
			@Override
			public void dataDeleted(String dataPath) throws Exception {
				// TODO Auto-generated method stub
				executorService.setCorePoolSize(Constants.AVAILABLE_PROCESSOR * 5);
				executorService.setMaximumPoolSize(Constants.AVAILABLE_PROCESSOR * 5);
				LOGGER.info("tip=参数调整"+ExtConfig.PUSH_THREAD+":"+Constants.AVAILABLE_PROCESSOR * 5);
			}
			
			@Override
			public void dataChange(String dataPath, Object data) throws Exception {
				// TODO Auto-generated method stub
                if(data != null) {
                    executorService.setCorePoolSize((Integer)data);
                    executorService.setMaximumPoolSize((Integer)data);
                    LOGGER.info("tip=参数调整"+ExtConfig.PUSH_THREAD+":"+data);
                }
			}
		});
    	
    	return executorService;
    	
    }

    public enum MsgPushResult {
        NO_MSG, // 没有消息可执行
        SUCCESS, //推送成功
        FAILED,      //推送失败
        SENT_ERROR
    }


}

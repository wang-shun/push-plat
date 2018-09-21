package com.lvmama.bms.pusher.processor;

import com.lvmama.bms.core.commons.utils.Callable;
import com.lvmama.bms.core.constant.Constants;
import com.lvmama.bms.core.domain.PushResult;
import com.lvmama.bms.core.domain.dto.MessageDTO;
import com.lvmama.bms.core.exception.JobTrackerNotFoundException;
import com.lvmama.bms.core.exception.RequestTimeoutException;
import com.lvmama.bms.core.failstore.FailStorePathBuilder;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.protocol.MsgProtos;
import com.lvmama.bms.core.protocol.command.MsgPushRequest;
import com.lvmama.bms.core.protocol.command.MsgPushedRequest;
import com.lvmama.bms.core.remoting.RemotingClientDelegate;
import com.lvmama.bms.core.support.NodeShutdownHook;
import com.lvmama.bms.core.support.RetryScheduler;
import com.lvmama.bms.core.support.SystemClock;
import com.lvmama.bms.pusher.domain.MsgPusherAppContext;
import com.lvmama.bms.pusher.domain.Response;
import com.lvmama.bms.pusher.expcetion.NoAvailableJobRunnerException;
import com.lvmama.bms.pusher.runner.RunnerCallback;
import com.lvmama.bms.remoting.AsyncCallback;
import com.lvmama.bms.remoting.Channel;
import com.lvmama.bms.remoting.ResponseFuture;
import com.lvmama.bms.remoting.exception.RemotingCommandException;
import com.lvmama.bms.remoting.protocol.RemotingCommand;
import com.lvmama.bms.remoting.protocol.RemotingProtos;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Robert HG (254963746@qq.com) on 8/14/14.
 *         接受任务并执行
 */
public class MsgPushProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgPushProcessor.class);

    private RetryScheduler<PushResult> retryScheduler;
    private PushRunnerCallback pushRunnerCallback;
    private RemotingClientDelegate remotingClient;

    private Logger logger = LoggerFactory.getLogger(MsgPushProcessor.class);

    protected MsgPushProcessor(MsgPusherAppContext appContext) {
        super(appContext);
        this.remotingClient = appContext.getRemotingClient();
        retryScheduler = new RetryScheduler<PushResult>(MsgPushProcessor.class.getSimpleName(), appContext,
                FailStorePathBuilder.getJobFeedbackPath(appContext), 3) {
            @Override
            protected boolean isRemotingEnable() {
                return remotingClient.isServerEnable();
            }

            @Override
            protected List<String> retry(List<String> keys, List<PushResult> results) {
                if(retrySendJobResults(results)) {
                    return keys;
                }
                return Collections.emptyList();
            }
        };
        retryScheduler.start();

        // 线程安全的
        pushRunnerCallback = new PushRunnerCallback();

        NodeShutdownHook.registerHook(appContext, this.getClass().getName(), new Callable() {
            @Override
            public void call() throws Exception {
                retryScheduler.stop();
            }
        });
    }

    @Override
    public RemotingCommand processRequest(Channel channel, final RemotingCommand request) throws RemotingCommandException {

        MsgPushRequest requestBody = request.getBody();
        MessageDTO message = requestBody.getMessage();

        try {
            appContext.getRunnerPool().execute(message, pushRunnerCallback);
        } catch (NoAvailableJobRunnerException e) {
            return RemotingCommand.createResponseCommand(MsgProtos.ResponseCode.NO_AVAILABLE_JOB_RUNNER.code(),
                    "tip=Message push failure , no available message runner!");
        }

        // 任务推送成功
        return RemotingCommand.createResponseCommand(MsgProtos
                .ResponseCode.JOB_PUSH_SUCCESS.code(), "Message push success!");
    }

    /**
     * 任务执行的回调(任务执行完之后线程回调这个函数)
     */
    private class PushRunnerCallback implements RunnerCallback {
        @Override
        public MessageDTO runComplete(Response response) {

            final PushResult pushResult = new PushResult();
            pushResult.setMessage(response.getMessage());
            pushResult.setAction(response.getAction());
            pushResult.setMsg(response.getMsg());
            pushResult.setTimeCost(response.getTimeCost());

            MsgPushedRequest requestBody = appContext.getCommandBodyWrapper().wrapper(new MsgPushedRequest());
            requestBody.setPushResults(Collections.singletonList(pushResult));
            requestBody.setReceiveNewMsg(response.isReceiveNewJob());     // 设置可以接受新任务
            requestBody.setSpeedFast(response.getMessage().isSpeedFast());

            RemotingCommand request = RemotingCommand.createRequestCommand(MsgProtos.RequestCode.JOB_COMPLETED.code(), requestBody);

            final Response returnResponse = new Response();

            try {
                final CountDownLatch latch = new CountDownLatch(1);
                remotingClient.invokeAsync(request, new AsyncCallback() {
                    @Override
                    public void operationComplete(ResponseFuture responseFuture) {
                        try {
                            RemotingCommand commandResponse = responseFuture.getResponseCommand();

                            if (commandResponse != null && commandResponse.getCode() == RemotingProtos.ResponseCode.SUCCESS.code()) {
                                MsgPushRequest msgPushRequest = commandResponse.getBody();
                                if (msgPushRequest != null) {
                                    if (LOGGER.isDebugEnabled()) {
                                        LOGGER.debug("Get new message :{}", msgPushRequest.getMessage());
                                    }
                                    returnResponse.setMessage(msgPushRequest.getMessage());
                                }
                            } else {
                                if (LOGGER.isInfoEnabled()) {
                                    LOGGER.info("Message push feedback failed, save local files。{}", pushResult);
                                }
                                try {
                                    retryScheduler.inSchedule(
                                            pushResult.getMessage().getMsgTokenId()+"_"+ SystemClock.now(),
                                            pushResult);
                                } catch (Exception e) {
                                    LOGGER.error("Message feedback failed", e);
                                }
                            }
                        } finally {
                            latch.countDown();
                        }
                    }
                });

                try {
                    latch.await(Constants.LATCH_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new RequestTimeoutException(e);
                }
            } catch (JobTrackerNotFoundException e) {
                try {
                    LOGGER.warn("No job tracker available! save local files.");
                    retryScheduler.inSchedule(
                            pushResult.getMessage().getToken().concat("_") + SystemClock.now(),
                            pushResult);
                } catch (Exception e1) {
                    LOGGER.error("Save files failed, {}", pushResult.getMessage(), e1);
                }
            }

            return returnResponse.getMessage();
        }
    }

    /**
     * 发送JobResults
     */
    private boolean retrySendJobResults(List<PushResult> results) {
        // 发送消息给 JobTracker
        MsgPushedRequest requestBody = appContext.getCommandBodyWrapper().wrapper(new MsgPushedRequest());
        requestBody.setPushResults(results);
        requestBody.setReSend(true);

        int requestCode = MsgProtos.RequestCode.JOB_COMPLETED.code();
        RemotingCommand request = RemotingCommand.createRequestCommand(requestCode, requestBody);

        try {
            // 这里一定要用同步，不然异步会发生文件锁，死锁
            RemotingCommand commandResponse = remotingClient.invokeSync(request);
            if (commandResponse != null && commandResponse.getCode() == RemotingProtos.ResponseCode.SUCCESS.code()) {
                return true;
            } else {
                LOGGER.warn("Send job failed, {}", commandResponse);
                return false;
            }
        } catch (JobTrackerNotFoundException e) {
            LOGGER.error("Retry send job result failed! jobResults={}", results, e);
        }
        return false;
    }

}

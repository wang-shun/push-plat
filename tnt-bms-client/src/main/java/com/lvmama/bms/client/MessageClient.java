package com.lvmama.bms.client;

import com.lvmama.bms.client.domain.MsgClientAppContext;
import com.lvmama.bms.client.domain.MsgClientNode;
import com.lvmama.bms.client.domain.Response;
import com.lvmama.bms.client.domain.ResponseCode;
import com.lvmama.bms.client.processor.RemotingDispatcher;
import com.lvmama.bms.client.support.MsgClientMStatReporter;
import com.lvmama.bms.client.support.MsgSendExecutor;
import com.lvmama.bms.client.support.MsgSendProtector;
import com.lvmama.bms.client.support.SendCallback;
import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.core.cluster.AbstractClientNode;
import com.lvmama.bms.core.commons.utils.BatchUtils;
import com.lvmama.bms.core.commons.utils.CollectionUtils;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.constant.Constants;
import com.lvmama.bms.core.constant.EcTopic;
import com.lvmama.bms.core.domain.Message;
import com.lvmama.bms.core.exception.JobTrackerNotFoundException;
import com.lvmama.bms.core.exception.MsgSendException;
import com.lvmama.bms.core.json.JSON;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.protocol.MsgProtos;
import com.lvmama.bms.core.protocol.command.CommandBodyWrapper;
import com.lvmama.bms.core.protocol.command.MsgSendRequest;
import com.lvmama.bms.core.protocol.command.MsgSendResponse;
import com.lvmama.bms.ec.EventInfo;
import com.lvmama.bms.ec.EventSubscriber;
import com.lvmama.bms.ec.Observer;
import com.lvmama.bms.remoting.AsyncCallback;
import com.lvmama.bms.remoting.RemotingProcessor;
import com.lvmama.bms.remoting.ResponseFuture;
import com.lvmama.bms.remoting.protocol.RemotingCommand;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MessageClient<T extends MsgClientNode, Context extends AppContext> extends
        AbstractClientNode<MsgClientNode, MsgClientAppContext> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MessageClient.class);

    private static final int BATCH_SIZE = 10;

    // 过载保护的提交者
    private MsgSendProtector protector;
    protected MsgClientMStatReporter stat;

    public MessageClient() {
        this.stat = new MsgClientMStatReporter(appContext);
        // 监控中心
        appContext.setMStatReporter(stat);
    }

    @Override
    protected void beforeStart() {
        appContext.setRemotingClient(remotingClient);
        protector = new MsgSendProtector(appContext);
    }

    @Override
    protected void afterStart() {

        appContext.getMStatReporter().start();

    }

    @Override
    protected void afterStop() {
        appContext.getMStatReporter().stop();
    }

    @Override
    protected void beforeStop() {
    }

    public Response sendMessage(Message message) throws MsgSendException {
        checkStart();
        return protectSend(Collections.singletonList(message));
    }

    private Response protectSend(List<Message> messages) throws MsgSendException {
        return protector.execute(messages, new MsgSendExecutor<Response>() {
            @Override
            public Response execute(List<Message> messages) throws MsgSendException {
                return sendMessage(MsgSendType.ASYNC, messages);
            }
        });
    }

    private void checkFields(List<Message> messages) {
        // 参数验证
        if (CollectionUtils.isEmpty(messages)) {
            throw new MsgSendException("Message can not be null!");
        }
        for (Message message : messages) {
            if (message == null) {
                throw new MsgSendException("Message can not be null!");
            } else {
                message.checkField();
            }
        }
    }

    protected Response sendMessage(MsgSendType type, final List<Message> messages) throws MsgSendException {
        // 检查参数
        checkFields(messages);

        final Response response = new Response();
        try {
            MsgSendRequest msgSubmitRequest = CommandBodyWrapper.wrapper(appContext, new MsgSendRequest());
            msgSubmitRequest.setMessages(messages);

            RemotingCommand requestCommand = RemotingCommand.createRequestCommand(
                    MsgProtos.RequestCode.SEND_MESSAGE.code(), msgSubmitRequest);

            SendCallback sendCallback = new SendCallback() {
                @Override
                public void call(RemotingCommand responseCommand) {
                    if (responseCommand == null) {
                        response.setFailedMessageWrappers(messages, true);
                        response.setSuccess(false);
                        response.setMsg("Send Message failed: Message Scheduler is broken");
                        LOGGER.warn("Send Message failed: {}, {}", messages, "Message Scheduler is broken");
                        return;
                    }

                    if (MsgProtos.ResponseCode.JOB_RECEIVE_SUCCESS.code() == responseCommand.getCode()) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Send Message success: {}", messages);
                        }
                        response.setSuccess(true);
                        return;
                    }
                    // 失败的job
                    MsgSendResponse msgSendResponse = responseCommand.getBody();
                    response.setFailedMessageWrappers(msgSendResponse.getFailedMessages());
                    response.setSuccess(false);
                    response.setCode(MsgProtos.ResponseCode.valueOf(responseCommand.getCode()).name());
                    response.setMsg("Send Message failed: " + responseCommand.getRemark());
                    LOGGER.warn("Send Message failed: {}, {}, {}", messages, responseCommand.getRemark(), msgSendResponse.getMsg());
                }
            };

            if (MsgSendType.ASYNC.equals(type)) {
                asyncSend(requestCommand, sendCallback);
            } else {
                syncSend(requestCommand, sendCallback);
            }
        } catch (JobTrackerNotFoundException e) {
            response.setSuccess(false);
            response.setCode(ResponseCode.MSG_SCHEDULER_NOT_FOUND);
            response.setMsg("Can not found Message scheduler node!");
            response.setFailedMessageWrappers(messages, true);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setCode(ResponseCode.SYSTEM_ERROR);
            response.setMsg(StringUtils.toString(e));
            response.setFailedMessageWrappers(messages, true);
        } finally {
            // 统计
            if (response.isSuccess()) {
                stat.incSubmitSuccessNum(messages.size());
            } else {
                stat.incSubmitFailedNum(CollectionUtils.sizeOf(response.getFailedMessages()));
            }
        }
        LOGGER.info("type=submit|status="+response.isSuccess()+"|messages="+ JSON.toJSONString(messages));
        return response;
    }

    /**
     * 异步提交任务
     */
    private void asyncSend(RemotingCommand requestCommand, final SendCallback submitCallback)
            throws JobTrackerNotFoundException {
        final CountDownLatch latch = new CountDownLatch(1);
        remotingClient.invokeAsync(requestCommand, new AsyncCallback() {
            @Override
            public void operationComplete(ResponseFuture responseFuture) {
                try {
                    submitCallback.call(responseFuture.getResponseCommand());
                } finally {
                    latch.countDown();
                }
            }
        });
        try {
            latch.await(Constants.LATCH_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new MsgSendException("Send Message failed, async request timeout!", e);
        }
    }

    /**
     * 同步提交任务
     */
    private void syncSend(RemotingCommand requestCommand, final SendCallback submitCallback)
            throws JobTrackerNotFoundException {
        submitCallback.call(remotingClient.invokeSync(requestCommand));
    }

    public Response sendMessage(List<Message> messages) throws MsgSendException {
        checkStart();
        final Response response = new Response();
        response.setSuccess(true);
        int size = messages.size();

        BatchUtils.batchExecute(size, BATCH_SIZE, messages, new BatchUtils.Executor<Message>() {
            @Override
            public boolean execute(List<Message> list) {
                Response subResponse = protectSend(list);
                if (!subResponse.isSuccess()) {
                    response.setSuccess(false);
                    response.addFailedMessageWrapper(subResponse.getFailedMessageWrappers());
                    response.setMsg(subResponse.getMsg());
                }
                return true;
            }
        });
        return response;
    }

    @Override
    protected RemotingProcessor getDefaultProcessor() {
        return new RemotingDispatcher(appContext);
    }

    enum MsgSendType {
        SYNC,   // 同步
        ASYNC   // 异步
    }

    private void checkStart() {
        if (!started.get()) {
            throw new MsgSendException("MessageClient did not started");
        }
    }

}

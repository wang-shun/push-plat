package com.lvmama.bms.client;

import com.lvmama.bms.client.domain.MsgClientAppContext;
import com.lvmama.bms.client.domain.MsgClientNode;
import com.lvmama.bms.client.domain.Response;
import com.lvmama.bms.client.domain.ResponseCode;
import com.lvmama.bms.client.support.MsgSendProtectException;
import com.lvmama.bms.core.domain.FailedMessageWrapper;
import com.lvmama.bms.core.domain.Message;
import com.lvmama.bms.core.failstore.FailStorePathBuilder;
import com.lvmama.bms.core.json.JSON;
import com.lvmama.bms.core.support.RetryScheduler;

import java.util.*;

/**
 * @author Robert HG (254963746@qq.com) on 8/14/14.
 *         重试 客户端, 如果 没有可用的JobTracker, 那么存文件, 定时重试
 */
public class RetryMessageClient extends MessageClient<MsgClientNode, MsgClientAppContext> {

    private RetryScheduler<Message> jobRetryScheduler;

    @Override
    protected void beforeStart() {
        super.beforeStart();
        jobRetryScheduler = new RetryScheduler<Message>(RetryMessageClient.class.getSimpleName(), appContext,
                FailStorePathBuilder.getJobSubmitFailStorePath(appContext), 10) {

            protected boolean isRemotingEnable() {
                return isServerEnable();
            }

            protected List<String> retry(List<String> keys, List<Message> messages) {
                Response response = null;
                try {

                    Set<String> retryKeySet = new HashSet<>(keys);

                    // 重试必须走同步，不然会造成文件锁，死锁
                    response = sendMessage(MsgSendType.SYNC, messages);

                    for(Iterator<FailedMessageWrapper> iterator = response.getFailedMessageWrappers().iterator(); iterator.hasNext();) {
                        FailedMessageWrapper failedMessageWrapper = iterator.next();
                        if(failedMessageWrapper.isTemporaryRetry()) {
                            Message failedMessage = failedMessageWrapper.getFailedMessage();
                            retryKeySet.remove(failedMessage.getMsgId()+"_"+failedMessage.getMsgType());
                        }
                    }

                    return new ArrayList<>(retryKeySet);

                } catch (Throwable t) {
                    RetryScheduler.LOGGER.error(t.getMessage(), t);
                } finally {
                    if (response != null && response.isSuccess()) {
                        stat.incSubmitFailStoreNum(messages.size());
                    }
                }

                return Collections.emptyList();

            }
        };
        jobRetryScheduler.start();
    }

    @Override
    protected void beforeStop() {
        super.beforeStop();
        jobRetryScheduler.stop();
    }

    @Override
    public Response sendMessage(Message message) {
        return sendMessage(Collections.singletonList(message));
    }

    @Override
    public Response sendMessage(List<Message> messages) {

        Response response;
        try {
            response = super.sendMessage(messages);
        } catch (MsgSendProtectException e) {
            response = new Response();
            response.setSuccess(false);
            response.setFailedMessageWrappers(messages, true);
            response.setCode(ResponseCode.SUBMIT_TOO_BUSY_AND_SAVE_FOR_LATER);
            response.setMsg(response.getMsg() + ", submit too busy");
        }
        if (!response.isSuccess()) {
            try {
                for(Iterator<FailedMessageWrapper> iterator = response.getFailedMessageWrappers().iterator(); iterator.hasNext();) {
                    FailedMessageWrapper failedMessageWrapper = iterator.next();
                    if(failedMessageWrapper.isTemporaryRetry()) {
                        //可重试消息
                        Message failedMessage = failedMessageWrapper.getFailedMessage();
                        jobRetryScheduler.inSchedule(failedMessage.getMsgId()+"-"+failedMessage.getMsgType(), failedMessage);
                        stat.incFailStoreNum();
                        iterator.remove();
                    }
                }

                if(response.getFailedMessageWrappers().size() == 0) {
                    response.setSuccess(true);
                    response.setCode(ResponseCode.SUBMIT_FAILED_AND_SAVE_FOR_LATER);
                    response.setMsg(response.getMsg() + ", save local fail store and send later !");
                }
                LOGGER.warn(JSON.toJSONString(response));
            } catch (Exception e) {
                response.setSuccess(false);
                response.setMsg(e.getMessage());
            }
        }

        return response;
    }


}

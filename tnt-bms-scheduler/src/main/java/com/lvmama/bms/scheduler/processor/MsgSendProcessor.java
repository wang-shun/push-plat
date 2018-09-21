package com.lvmama.bms.scheduler.processor;

import com.lvmama.bms.core.exception.MsgReceiveException;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.protocol.MsgProtos;
import com.lvmama.bms.core.protocol.command.MsgSendRequest;
import com.lvmama.bms.core.protocol.command.MsgSendResponse;
import com.lvmama.bms.remoting.Channel;
import com.lvmama.bms.remoting.exception.RemotingCommandException;
import com.lvmama.bms.remoting.protocol.RemotingCommand;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;

/**
 * @author Robert HG (254963746@qq.com) on 7/24/14.
 *         客户端提交任务的处理器
 */
public class MsgSendProcessor extends AbstractRemotingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgSendProcessor.class);

    public MsgSendProcessor(MsgSchedulerAppContext appContext) {
        super(appContext);
    }

    @Override
    public RemotingCommand processRequest(Channel channel, RemotingCommand request) throws RemotingCommandException {

        MsgSendRequest msgSubmitRequest = request.getBody();
        MsgSendResponse msgSubmitResponse = appContext.getCommandBodyWrapper().wrapper(new MsgSendResponse());

        try {
            appContext.getMessageReceiver().receive(msgSubmitRequest);
            return RemotingCommand.createResponseCommand(
                    MsgProtos.ResponseCode.JOB_RECEIVE_SUCCESS.code(), "message send success!", msgSubmitResponse);

        } catch (MsgReceiveException e) {
            msgSubmitResponse.setSuccess(false);
            msgSubmitResponse.setFailedMessages(e.getFailedMessages());
            return RemotingCommand.createResponseCommand(
                    MsgProtos.ResponseCode.JOB_RECEIVE_FAILED.code(), e.getMessage(), msgSubmitResponse);
        }

    }
}

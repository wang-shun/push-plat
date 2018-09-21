package com.lvmama.bms.scheduler.complete.biz.impl;

import com.lvmama.bms.core.domain.dto.MessageDTO;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.protocol.command.MsgPushRequest;
import com.lvmama.bms.core.protocol.command.MsgPushedRequest;
import com.lvmama.bms.remoting.protocol.RemotingCommand;
import com.lvmama.bms.remoting.protocol.RemotingProtos;
import com.lvmama.bms.scheduler.complete.biz.PushBiz;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.support.MessageDispatcher;

/**
 * 接受新任务Chain
 *
 * @author Robert HG (254963746@qq.com) on 11/11/15.
 */
public class PushNewBiz implements PushBiz {

    protected static final Logger LOGGER = LoggerFactory.getLogger(PushNewBiz.class);

    private MessageDispatcher messageDispatcher;
    private MsgSchedulerAppContext appContext;

    public PushNewBiz(MsgSchedulerAppContext appContext) {
        this.appContext = appContext;
        this.messageDispatcher = appContext.getMessageDispatcher();
    }

    @Override
    public RemotingCommand doBiz(MsgPushedRequest request) {
        // 判断是否接受新消息
        if (request.isReceiveNewMsg()) {
            try {
                // 查看有没有其他待发送消息
                MessageDTO message = messageDispatcher.getNextMessage(request.isSpeedFast()?
                        MessageDispatcher.MessageSpeed.Fast : MessageDispatcher.MessageSpeed.Slow);
                MsgPushRequest msgPushRequest = appContext.getCommandBodyWrapper().wrapper(new MsgPushRequest());
                msgPushRequest.setMessage(message);
                LOGGER.info("tip=push new message|"+message);
                // 返回 新的消息
                return RemotingCommand.createResponseCommand(RemotingProtos.ResponseCode.SUCCESS.code(), msgPushRequest);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    @Override
    public boolean isAsync() {
        return false;
    }
}

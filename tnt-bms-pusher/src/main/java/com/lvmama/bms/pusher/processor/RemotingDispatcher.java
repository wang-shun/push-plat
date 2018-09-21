package com.lvmama.bms.pusher.processor;

import com.lvmama.bms.core.protocol.MsgProtos;
import com.lvmama.bms.remoting.Channel;
import com.lvmama.bms.remoting.RemotingProcessor;
import com.lvmama.bms.remoting.exception.RemotingCommandException;
import com.lvmama.bms.remoting.protocol.RemotingCommand;
import com.lvmama.bms.remoting.protocol.RemotingProtos;
import com.lvmama.bms.pusher.domain.MsgPusherAppContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Robert HG (254963746@qq.com) on 8/14/14.
 *         task tracker 总的处理器, 每一种命令对应不同的处理器
 */
public class RemotingDispatcher extends AbstractProcessor {

    private final Map<MsgProtos.RequestCode, RemotingProcessor> processors = new HashMap<MsgProtos.RequestCode, RemotingProcessor>();

    public RemotingDispatcher(MsgPusherAppContext appContext) {
        super(appContext);
        processors.put(MsgProtos.RequestCode.PUSH_JOB, new MsgPushProcessor(appContext));
//        processors.put(MsgProtos.RequestCode.JOB_ASK, new MsgAskProcessor(appContext));
    }

    @Override
    public RemotingCommand processRequest(Channel channel, RemotingCommand request) throws RemotingCommandException {

        MsgProtos.RequestCode code = MsgProtos.RequestCode.valueOf(request.getCode());
        RemotingProcessor processor = processors.get(code);
        if (processor == null) {
            return RemotingCommand.createResponseCommand(RemotingProtos.ResponseCode.REQUEST_CODE_NOT_SUPPORTED.code(),
                    "request code not supported!");
        }
        return processor.processRequest(channel, request);
    }

}

package com.lvmama.bms.client.processor;

import com.lvmama.bms.core.protocol.MsgProtos;
import com.lvmama.bms.client.domain.MsgClientAppContext;
import com.lvmama.bms.remoting.Channel;
import com.lvmama.bms.remoting.RemotingProcessor;
import com.lvmama.bms.remoting.exception.RemotingCommandException;
import com.lvmama.bms.remoting.protocol.RemotingCommand;
import com.lvmama.bms.remoting.protocol.RemotingProtos;

import java.util.HashMap;
import java.util.Map;

import static com.lvmama.bms.core.protocol.MsgProtos.RequestCode.valueOf;

/**
 * @author Robert HG (254963746@qq.com) on 7/25/14.
 *         客户端默认通信处理器
 */
public class RemotingDispatcher implements RemotingProcessor {

    private final Map<MsgProtos.RequestCode, RemotingProcessor> processors = new HashMap<MsgProtos.RequestCode, RemotingProcessor>();

    public RemotingDispatcher(MsgClientAppContext appContext) {
    }

    @Override
    public RemotingCommand processRequest(Channel channel, RemotingCommand request) throws RemotingCommandException {
        MsgProtos.RequestCode code = valueOf(request.getCode());
        RemotingProcessor processor = processors.get(code);
        if (processor == null) {
            return RemotingCommand.createResponseCommand(RemotingProtos.ResponseCode.REQUEST_CODE_NOT_SUPPORTED.code(), "request code not supported!");
        }
        return processor.processRequest(channel, request);
    }
}

package com.lvmama.bms.scheduler.processor;

import com.lvmama.bms.core.protocol.command.MsgPushedRequest;
import com.lvmama.bms.remoting.Channel;
import com.lvmama.bms.remoting.exception.RemotingCommandException;
import com.lvmama.bms.remoting.protocol.RemotingCommand;
import com.lvmama.bms.remoting.protocol.RemotingProtos;
import com.lvmama.bms.scheduler.complete.biz.PushBiz;
import com.lvmama.bms.scheduler.complete.biz.impl.PushBreakBiz;
import com.lvmama.bms.scheduler.complete.biz.impl.PushNewBiz;
import com.lvmama.bms.scheduler.complete.biz.impl.PushProcBiz;
import com.lvmama.bms.scheduler.complete.biz.impl.PushSlowBiz;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Robert HG (254963746@qq.com) on 8/17/14.
 *         TaskTracker 完成任务 的处理器
 */
public class MsgPushedProcessor extends AbstractRemotingProcessor {

    private List<PushBiz> bizChain;

    public MsgPushedProcessor(final MsgSchedulerAppContext appContext) {
        super(appContext);
        this.bizChain = new CopyOnWriteArrayList<PushBiz>();
        this.bizChain.add(new PushProcBiz(appContext));           //完成处理
        this.bizChain.add(new PushSlowBiz(appContext));         //慢消息处理
        this.bizChain.add(new PushBreakBiz(appContext));        //推送熔断恢复
        this.bizChain.add(new PushNewBiz(appContext));            //获取新任务
    }

    @Override
    public RemotingCommand processRequest(Channel channel, RemotingCommand request)
            throws RemotingCommandException {

        MsgPushedRequest requestBody = request.getBody();

        for (PushBiz biz : bizChain) {
            RemotingCommand remotingCommand = biz.doBiz(requestBody);
            if (remotingCommand != null) {
                return remotingCommand;
            }
        }
        return RemotingCommand.createResponseCommand(RemotingProtos.ResponseCode.SUCCESS.code());
    }

}

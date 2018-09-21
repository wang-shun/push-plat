package com.lvmama.bms.scheduler.processor;

import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.protocol.MsgProtos;
import com.lvmama.bms.core.protocol.command.MsgPullRequest;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.support.MessagePusher;
import com.lvmama.bms.remoting.Channel;
import com.lvmama.bms.remoting.exception.RemotingCommandException;
import com.lvmama.bms.remoting.protocol.RemotingCommand;

/**
 * @author Robert HG (254963746@qq.com) on 7/24/14.
 *         处理 TaskTracker的 Job pull 请求
 */
public class MsgPullProcessor extends AbstractRemotingProcessor {

    private MessagePusher messagePusher;

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgPullProcessor.class);

    public MsgPullProcessor(MsgSchedulerAppContext appContext) {
        super(appContext);
        messagePusher = new MessagePusher(appContext);
    }

    @Override
    public RemotingCommand processRequest(final Channel ctx, final RemotingCommand request) throws RemotingCommandException {

        MsgPullRequest requestBody = request.getBody();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("taskTrackerNodeGroup:{}, taskTrackerIdentity:{} , fastIdleThreads:{}, slowIdleThreads:{}",
                    requestBody.getNodeGroup(), requestBody.getIdentity(), requestBody.getFastIdleThreads(), requestBody.getSlowIdleThreads());
        }
        messagePusher.concurrentPush(requestBody);

        return RemotingCommand.createResponseCommand(MsgProtos.ResponseCode.JOB_PULL_SUCCESS.code(), "");
    }
}

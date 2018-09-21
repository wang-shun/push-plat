package com.lvmama.bms.scheduler.cmd;

import com.lvmama.bms.cmd.HttpCmdProc;
import com.lvmama.bms.cmd.HttpCmdRequest;
import com.lvmama.bms.cmd.HttpCmdResponse;
import com.lvmama.bms.core.cmd.HttpCmdNames;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;

/**
 * 用来手动触发任务
 * 内部做的事情就是将某个任务加载到内存中
 * @author Robert HG (254963746@qq.com) on 8/4/16.
 */
public class TriggerJobManuallyHttpCmd implements HttpCmdProc {

    private final Logger LOGGER = LoggerFactory.getLogger(TriggerJobManuallyHttpCmd.class);

    private MsgSchedulerAppContext appContext;

    public TriggerJobManuallyHttpCmd(MsgSchedulerAppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public String nodeIdentity() {
        return appContext.getConfig().getIdentity();
    }

    @Override
    public String getCommand() {
        return HttpCmdNames.HTTP_CMD_TRIGGER_JOB_MANUALLY;
    }

    @Override
    public HttpCmdResponse execute(HttpCmdRequest request) throws Exception {
        String taskTrackerNodeGroup = request.getParam("nodeGroup");
        String jobId = request.getParam("jobId");

        if (StringUtils.isEmpty(taskTrackerNodeGroup)) {
            return HttpCmdResponse.newResponse(true, "nodeGroup should not be empty");
        }

        if (StringUtils.isEmpty(jobId)) {
            return HttpCmdResponse.newResponse(true, "jobId should not be empty");
        }

//        appContext.getPreLoader().loadOne2First(taskTrackerNodeGroup, jobId);

        LOGGER.info("Trigger Job jobId={} taskTrackerNodeGroup={}", jobId, taskTrackerNodeGroup);

        return HttpCmdResponse.newResponse(true, "trigger job succeed");
    }
}

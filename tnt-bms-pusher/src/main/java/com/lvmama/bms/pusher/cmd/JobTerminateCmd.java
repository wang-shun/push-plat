package com.lvmama.bms.pusher.cmd;

import com.lvmama.bms.cmd.HttpCmdProc;
import com.lvmama.bms.cmd.HttpCmdRequest;
import com.lvmama.bms.cmd.HttpCmdResponse;
import com.lvmama.bms.core.cmd.HttpCmdNames;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.pusher.domain.MsgPusherAppContext;

/**
 * 用于中断某个Job
 * @author Robert HG (254963746@qq.com) on 3/13/16.
 */
public class JobTerminateCmd implements HttpCmdProc {

    private MsgPusherAppContext appContext;

    public JobTerminateCmd(MsgPusherAppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public String nodeIdentity() {
        return appContext.getConfig().getIdentity();
    }

    @Override
    public String getCommand() {
        return HttpCmdNames.HTTP_CMD_JOB_TERMINATE;
    }

    @Override
    public HttpCmdResponse execute(HttpCmdRequest request) throws Exception {

        String jobId = request.getParam("jobId");
        if (StringUtils.isEmpty(jobId)) {
            return HttpCmdResponse.newResponse(false, "jobId can't be empty");
        }

//        if (!appContext.getRunnerPool().getRunningJobManager().running(jobId)) {
//            return HttpCmdResponse.newResponse(false, "jobId dose not running in this TaskTracker now");
//        }
//
//        appContext.getRunnerPool().getRunningJobManager().terminateJob(jobId);

        return HttpCmdResponse.newResponse(true, "Execute terminate Command success");
    }
}

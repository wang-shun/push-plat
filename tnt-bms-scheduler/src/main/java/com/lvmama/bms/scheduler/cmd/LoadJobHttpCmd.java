package com.lvmama.bms.scheduler.cmd;

import com.lvmama.bms.cmd.HttpCmdProc;
import com.lvmama.bms.cmd.HttpCmdRequest;
import com.lvmama.bms.cmd.HttpCmdResponse;
import com.lvmama.bms.core.cmd.HttpCmdNames;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;

/**
 * 给JobTracker发送信号，加载任务
 *
 * @author Robert HG (254963746@qq.com) on 10/26/15.
 */
public class LoadJobHttpCmd implements HttpCmdProc {

    private final Logger LOGGER = LoggerFactory.getLogger(LoadJobHttpCmd.class);

    private MsgSchedulerAppContext appContext;

    public LoadJobHttpCmd(MsgSchedulerAppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public String nodeIdentity() {
        return appContext.getConfig().getIdentity();
    }

    @Override
    public String getCommand() {
        return HttpCmdNames.HTTP_CMD_LOAD_JOB;
    }

    @Override
    public HttpCmdResponse execute(HttpCmdRequest request) throws Exception {

        String taskTrackerNodeGroup = request.getParam("nodeGroup");
//        appContext.getPreLoader().load(taskTrackerNodeGroup);

        LOGGER.info("load job succeed : nodeGroup={}", taskTrackerNodeGroup);

        return HttpCmdResponse.newResponse(true, "load job succeed");
    }
}

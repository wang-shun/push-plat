package com.lvmama.bms.scheduler.cmd;

import com.lvmama.bms.cmd.HttpCmdProc;
import com.lvmama.bms.cmd.HttpCmdRequest;
import com.lvmama.bms.cmd.HttpCmdResponse;
import com.lvmama.bms.core.cmd.HttpCmdNames;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;

/**
 * 添加任务
 *
 * @author Robert HG (254963746@qq.com) on 10/27/15.
 */
public class AddJobHttpCmd implements HttpCmdProc {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddJobHttpCmd.class);

    private MsgSchedulerAppContext appContext;

    public AddJobHttpCmd(MsgSchedulerAppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public String nodeIdentity() {
        return appContext.getConfig().getIdentity();
    }

    @Override
    public String getCommand() {
        return HttpCmdNames.HTTP_CMD_ADD_JOB;
    }

    @Override
    public HttpCmdResponse execute(HttpCmdRequest request) throws Exception {

        HttpCmdResponse response = new HttpCmdResponse();
        response.setSuccess(false);
        return response;
    }

}

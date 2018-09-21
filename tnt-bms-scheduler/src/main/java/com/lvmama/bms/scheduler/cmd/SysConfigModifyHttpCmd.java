package com.lvmama.bms.scheduler.cmd;

import com.lvmama.bms.cmd.HttpCmdProc;
import com.lvmama.bms.cmd.HttpCmdRequest;
import com.lvmama.bms.cmd.HttpCmdResponse;

/**
 * 一些系统配置更改CMD
 * @author Robert HG (254963746@qq.com) on 3/16/16.
 */
public class SysConfigModifyHttpCmd implements HttpCmdProc {

    @Override
    public String nodeIdentity() {
        return null;
    }

    @Override
    public String getCommand() {
        return null;
    }

    @Override
    public HttpCmdResponse execute(HttpCmdRequest request) throws Exception {
        return null;
    }
}

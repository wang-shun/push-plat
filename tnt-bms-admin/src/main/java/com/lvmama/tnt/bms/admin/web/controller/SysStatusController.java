package com.lvmama.tnt.bms.admin.web.controller;

import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.tnt.bms.admin.web.service.SendConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/systatus")
public class SysStatusController {

    @Autowired
    private SendConfigService sendConfigService;

    @RequestMapping("/switchTaskAssign")
    public boolean switchTaskAssign(@RequestParam(required = false, defaultValue = "true") boolean isTurnOff) {
        sendConfigService.sendMessage(SyncEvent.ObjectType.TASK_ASSIGN,
                isTurnOff ? SyncEvent.EventType.TurnOff : SyncEvent.EventType.TurnOn, "");
        return true;
    }

    @RequestMapping("/flushTaskAssign")
        public boolean flushTaskAssign() {
        sendConfigService.sendMessage(SyncEvent.ObjectType.TASK_ASSIGN, SyncEvent.EventType.Assign, "");
        return true;
    }

}

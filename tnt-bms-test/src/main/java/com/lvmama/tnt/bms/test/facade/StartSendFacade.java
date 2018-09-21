package com.lvmama.tnt.bms.test.facade;

import com.lvmama.tnt.bms.test.service.IStartSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class StartSendFacade {

    @Autowired
    private IStartSendService startSendService;

    @RequestMapping("/send/start")
    public String startSend() {
        startSendService.startSend();
        return "success";
    }

    @RequestMapping("/send/cancel")
    public String cancelSend() {
        startSendService.cancelSend();
        return "success";
    }

    @RequestMapping("/send/one")
    public String sendOneTime() {
        startSendService.sendOneTime();
        return "success";
    }

    @RequestMapping("/send/{minute}")
    public String sendFixedTime(@PathVariable int minute) {
        startSendService.sendFixedTime(minute);
        return "success";
    }

    @RequestMapping("/send/oneThread")
    public String sendOneThread() {
        startSendService.sendOneThread();
        return "success";
    }

}

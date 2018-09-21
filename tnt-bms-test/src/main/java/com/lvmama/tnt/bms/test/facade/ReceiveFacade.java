package com.lvmama.tnt.bms.test.facade;

import com.alibaba.fastjson.JSON;
import com.lvmama.tnt.bms.test.domain.vo.ResponseVO;
import com.lvmama.tnt.bms.test.service.IReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class ReceiveFacade {

    @Autowired
    IReceiveService receiveService;

    @RequestMapping("/receive0")
    public String receive0(@RequestBody String dataStr) {
        receiveService.save(dataStr);
        return JSON.toJSONString(new ResponseVO("10000", "success"));
    }

    @RequestMapping("/receive1")
    public String receive1(@RequestBody String dataStr) {
        receiveService.save(dataStr);
        return JSON.toJSONString(new ResponseVO("10000", "success"));
    }

    @RequestMapping("/receive2")
    public String receive2(@RequestBody String dataStr) {
        receiveService.save(dataStr);
        return JSON.toJSONString(new ResponseVO("10000", "success"));
    }

    @RequestMapping("/receive3")
    public String receive3(@RequestBody String dataStr) {
        receiveService.save(dataStr);
        return JSON.toJSONString(new ResponseVO("10000", "success"));
    }

    @RequestMapping("/receive4")
    public String receive4(@RequestBody String dataStr) {
        receiveService.save(dataStr);
        return JSON.toJSONString(new ResponseVO("10000", "success"));
    }

    @RequestMapping("/receive5")
    public String receive5(@RequestBody String dataStr) {
        receiveService.save(dataStr);
        return JSON.toJSONString(new ResponseVO("10000", "success"));
    }

    @RequestMapping("/receive6")
    public String receive6(@RequestBody String dataStr) {
        receiveService.save(dataStr);
        return JSON.toJSONString(new ResponseVO("10000", "success"));
    }

    @RequestMapping("/receive7")
    public String receive7(@RequestBody String dataStr) {
        receiveService.save(dataStr);
        return JSON.toJSONString(new ResponseVO("10000", "success"));
    }

    @RequestMapping("/receive8")
    public String receive8(@RequestBody String dataStr) {
        receiveService.save(dataStr);
        return JSON.toJSONString(new ResponseVO("10000", "success"));
    }

    @RequestMapping("/receive9")
    public String receive9(@RequestBody String dataStr) {
        receiveService.save(dataStr);
        return JSON.toJSONString(new ResponseVO("10000", "success"));
    }

}

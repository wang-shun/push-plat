package com.lvmama.tnt.bms.test.facade;

import com.lvmama.tnt.bms.test.service.IRegisterApiDistributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class RegistApiFacade {

    @Autowired
    private IRegisterApiDistributorService registerApiDistributorService;

    @RequestMapping("/api/register")
    public String register() {
        registerApiDistributorService.register();
        return "success!";
    }


    @RequestMapping("/api/init")
    public String initData() {
        registerApiDistributorService.initUrlMap();
        registerApiDistributorService.initConvert();
        return "success!";
    }
}

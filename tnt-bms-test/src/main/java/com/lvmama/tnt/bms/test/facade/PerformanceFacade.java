package com.lvmama.tnt.bms.test.facade;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
public class PerformanceFacade {

    @RequestMapping("")
    public String performance(String token) {
        return null;
    }


}

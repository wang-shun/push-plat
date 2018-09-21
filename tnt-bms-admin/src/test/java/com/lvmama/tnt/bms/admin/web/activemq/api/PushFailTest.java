package com.lvmama.tnt.bms.admin.web.activemq.api;

import com.lvmama.boot.core.App;
import com.lvmama.tnt.bms.admin.web.Main;
import com.lvmama.tnt.bms.admin.web.domain.po.PushFailureDO;
import com.lvmama.tnt.bms.admin.web.mapper.PusherManagerMapper;
import com.lvmama.tnt.bms.admin.web.service.PushFailureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class)
public class PushFailTest {

    static {
        App.setProfileIfNotExists("dev");
    }

    @Autowired
    private PushFailureService pushFailureService;

    @Test
    public void test() {

    }
}

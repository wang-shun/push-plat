package com.lvmama.tnt.bms.admin.web;

import com.lvmama.bms.core.json.JSON;
import com.lvmama.boot.core.App;
import com.lvmama.boot.dubbo.boot.DubboProperties;
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
public class DubboTest {

    static {
        App.setProfileIfNotExists("ARK");
    }

    @Autowired
    private DubboProperties dubboProperties;

    @Test
    public void test() {
        System.out.println(JSON.toJSONString(dubboProperties));
    }


}

package com.lvmama.tnt.bms.admin.web;

import com.lvmama.boot.core.App;
import com.lvmama.tnt.bms.admin.web.sendMail.EmailEntity;
import com.lvmama.tnt.bms.admin.web.sendMail.MailClient;
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
public class MailTest {

    static {
        App.setProfileIfNotExists("ARK");
    }

    @Autowired
    private MailClient mailConfiguration;

    @Test
    public void test() {
        System.setProperty("java.net.preferIPv4Stack", "true");
        EmailEntity entity = new EmailEntity();
//        entity.setSender("bmsAlert01@163.com");
//        entity.setReceiver("longhairen@lvmama.com;lhr9563215@163.com");
        entity.setContent("商家失败率");
        entity.setSubject("发送失败率统计");
        mailConfiguration.sendSimpleMail(entity);
    }
}

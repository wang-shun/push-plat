package com.lvmama.tnt.bms.admin.web;

import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.tnt.bms.admin.web.domain.define.MessageConstants;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import com.lvmama.tnt.bms.admin.web.service.SendConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author longhr
 * @version 2017/11/21 0021
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class)
public class SendMessageTest {

    @Autowired
    private SendConfigService sendConfigService;

    @Test
    public void sendTest() {
        NewsTypeDO newsTypeDO = new NewsTypeDO();
        newsTypeDO.setType("hello");
        newsTypeDO.setPriority(2);
        newsTypeDO.setOpened(1);
        sendConfigService.sendMessage(SyncEvent.ObjectType.MANUAL_PUSH, SyncEvent.EventType.Add,newsTypeDO);
    }

    public static void main(String[] args){
        String destination_template = "TNT_BMS_ACTIVEMQ_%s_MESSAGE";
        System.out.println(String.format(destination_template, MessageConstants.Type.ACCESS.name()));
    }

}



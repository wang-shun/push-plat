package com.lvmama.tnt.bms.admin.web;

import com.lvmama.bms.core.json.JSON;
import com.lvmama.bms.core.json.TypeReference;
import com.lvmama.tnt.bms.admin.web.domain.po.Order;
import com.lvmama.tnt.bms.admin.web.mapper.ReceivedMapper;
import com.lvmama.tnt.bms.admin.web.util.JaxbUtil;
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
public class XmlMapTest {

    private static String xmlString = "<order><orderId>479</orderId><amount>699</amount><status>success</status><product>南山文化旅游区(181731)</product><channel>B2B</channel><payway>online</payway><company>广州分公司</company><visitor>张三</visitor><createUser>李四</createUser></order>";
    private static String jsonString = "{\"amount\":98,\"product\":\"南山文化旅游区(181731)\",\"orderId\":611,\"channel\":\"B2B\",\"name\":\"order\",\"payway\":\"online\",\"company\":\"广州分公司\",\"createUser\":\"李四\",\"visitor\":\"张三\",\"status\":\"success\"}";

    @Autowired
    private ReceivedMapper mapper;

    @Test
    public void test(){
        Order order = JaxbUtil.unmarshToObjBinding(Order.class, xmlString);
        System.out.println(order.toString());
        mapper.save(order);

        System.out.println(JSON.parse(jsonString, new TypeReference<Order>(){}).toString());
    }
}

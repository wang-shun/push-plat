package com.lvmama.tnt.bms.admin.web;

import com.lvmama.tnt.bms.admin.web.util.HttpUtil;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author longhr
 * @version 2017/11/29 0029
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class)
public class ActivemqTest {



    public static void main(String[] args) throws Exception{
        String url = "http://localhost:8080/tnt_push_service/monitor/unpushed";
        String resp = HttpUtil.sendPost(url, "a1234567!");

        System.out.println(resp);

    }
}

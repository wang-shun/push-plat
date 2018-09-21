package com.lvmama;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lvmama.bms.extend.rpc.Message;
import ognl.Ognl;
import ognl.OgnlException;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestOgnl {

    @Test
    public void testContext() {

        Map<String, Object> context = new HashMap<>();
        context.put("name", "lisi");

        Map<String, Object> root = new HashMap<>();
        root.put("name", "zhsan");

        root.put("user", context);

        try {
//            System.out.println(Ognl.getValue("name", context, root));
//            System.out.println(Ognl.getValue("#name", context, root));
//            System.out.println(Ognl.getValue("user.name", context, root));
            System.out.println(Ognl.getValue("test.user.age+user.name", context, root));
        } catch (OgnlException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testOgnl() {

        Message message = new Message();
        message.setMsgId("msgId101");
        message.setMsgType("msgType102");

        List<String> list = Arrays.asList("100", "102");

        try {
            System.out.println(Ognl.getValue("msgId+',test+'+11+msgType", message));
            System.out.println(Ognl.getValue("iterator.next+','+iterator.next", list));
        } catch (OgnlException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testJson() {

        JSONObject parent = new JSONObject();
        parent.put("name", "lisi");
        parent.put("age", 20);

        JSONObject child = new JSONObject();
        child.put("name", "zhsan");
        child.put("age", 10);

        parent.put("child", child);


        try {
            System.out.println(Ognl.getValue("name", parent));

            System.out.println(Ognl.getValue("child.name", parent));
        } catch (OgnlException e) {
            e.printStackTrace();
        }

    }

}

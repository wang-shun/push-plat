package com.lvmama.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lvmama.bms.client.MessageClient;
import com.lvmama.bms.client.RetryMessageClient;
import com.lvmama.bms.client.domain.Response;
import com.lvmama.bms.core.domain.Message;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

public class TestConverterVersion2 {

    private static MessageClient messageClient;

    @BeforeClass
    public static void init() {
        messageClient = new RetryMessageClient();
        messageClient.setClusterName("tnt_bms_cluster");
        messageClient.setRegistryAddress("zookeeper://127.0.0.1:2181");
        messageClient.start();
    }

    @Test
    public void testXmlVersion() {

        Message message = new Message();
        message.setMsgId(UUID.randomUUID().toString());
        message.setMsgType("product");
        message.setTokens(new String[] {"25459B447AE644AA92FE3E2D45207E19"});

        //消息内容，Object类型
        JSONObject payload = new JSONObject();
        payload.put("changeType", "product");
        JSONArray prouducts = new JSONArray();
        for(int i = 0; i < 3; i++) {
            JSONObject product = new JSONObject();
            product.put("productId", "productId"+i);
            product.put("goodsId", "goodsId"+i);
            prouducts.add(product);
        }

        payload.put("products", prouducts);
        message.setPayload(payload);

        message.setReplaceOnExist(false);
        message.setMaxRetry(3);
        Response response = messageClient.sendMessage(message);
        System.out.println(response);

    }

    @Test
    public void testFormVersion() {

        Message message = new Message();
        message.setMsgId(UUID.randomUUID().toString());
        message.setMsgType("product");
        message.setTokens(new String[]{"AF84757C8CC5421B917A415BBC2E6D02"});
        message.setPayload(JSON.parseArray("[{\"productId\":2987715,\"changeIds\":4167035,\"goodsId\":4167035},{\"productId\":29877151,\"changeIds\":41670351,\"goodsId\":41670351}]"));
        message.setReplaceOnExist(false);
        message.setMaxRetry(3);
        Response response = messageClient.sendMessage(message);
        System.out.println(response);


    }

    @Test
    public void testJsonVersion() {

        Message message = new Message();
        message.setMsgId(UUID.randomUUID().toString());
        message.setMsgType("product");
        message.setTokens(new String[] {"3E7656A2D07C45F4BAEDD2FB3AE16C1C"});

        //消息内容，Object类型
        JSONObject payload = new JSONObject();
        payload.put("changeType", "product");
        JSONArray prouducts = new JSONArray();
        for(int i = 0; i < 3; i++) {
            JSONObject product = new JSONObject();
            product.put("productId", "productId"+i);
            product.put("goodsId", "goodsId"+i);
            prouducts.add(product);
        }

        payload.put("products", prouducts);
        message.setPayload(payload);

        message.setReplaceOnExist(false);
        message.setMaxRetry(3);
        Response response = messageClient.sendMessage(message);
        System.out.println(response);

    }




}

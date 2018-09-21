package com.lvmama.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lvmama.bms.client.MessageClient;
import com.lvmama.bms.client.RetryMessageClient;
import com.lvmama.bms.client.domain.Response;
import com.lvmama.bms.core.domain.Message;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class TestMsgClient {

    private static MessageClient messageClient;

    @BeforeClass
    public static void init() {
        messageClient = new RetryMessageClient();
        messageClient.setClusterName("tnt_bms_cluster");
        messageClient.setRegistryAddress("zookeeper://127.0.0.1:2181");
        messageClient.start();
    }

    @Test
    public void sendMessage() {

        for(int i = 0; i < 1; i++) {

            Message message = new Message();
            message.setMsgId(UUID.randomUUID().toString());
            message.setMsgType("product1");
            message.setTokens(new String[] {"E80472A800864624BF44F2A45463277D"});

            JSONObject payload = new JSONObject();
            payload.put("name", "lisi");
            payload.put("age", 30);
            message.setPayload(payload);

            message.setReplaceOnExist(false);
            message.setMaxRetry(3);
            Response response = messageClient.sendMessage(message);
            System.out.println(response);

        }

    }

    @Test
    public void sendBatchMessage() {

        List<Message> list = new ArrayList<>();

        Message message = new Message();
        message.setMsgId(UUID.randomUUID().toString());
        message.setMsgType("product1");
        message.setTokens(new String[] {"E80472A800864624BF44F2A45463277D"});
        JSONObject payload = new JSONObject();
        payload.put("name", "lisi");
        payload.put("age", 30);
        message.setPayload(payload);
        message.setReplaceOnExist(false);
        message.setMaxRetry(3);

        list.add(message);

        message = new Message();
        message.setMsgId(UUID.randomUUID().toString());
        message.setMsgType("product2");
        message.setTokens(new String[] {"E80472A800864624BF44F2A45463277D"});
        payload = new JSONObject();
        payload.put("name", "lisi");
        payload.put("age", 30);
        message.setPayload(payload);
        message.setReplaceOnExist(false);
        message.setMaxRetry(3);

        list.add(message);


        Response response = messageClient.sendMessage(list);
        System.out.println(response);

        try {
            CountDownLatch latch = new CountDownLatch(1);
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}

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

public class TestConverterVersion1 {

    private static MessageClient messageClient;

    @BeforeClass
    public static void init() {
        messageClient = new RetryMessageClient();
        messageClient.setClusterName("tnt_bms_cluster");
        messageClient.setRegistryAddress("zookeeper://127.0.0.1:2181");
        messageClient.start();
    }

    private void sendMessage(String token) {

        Message message = new Message();
        message.setMsgId(UUID.randomUUID().toString());
        message.setMsgType("product");
        message.setTokens(new String[] {token});
        String json = "{\"header\":{},\"body\":{\"order\":{\"orderId\":\"62986544\",\"policies\":[{\"credentials\":\"411522198803123782\",\"name\":\"测试\",\"policyStatus\":\"REQUEST_CANCLE\", \"policyNumber\":100, \"remarks\":\"tzh测试\"},{\"credentials\":\"311522198803123782\",\"name\":\"测2\",\"policyStatus\":\"REQUEST_CANCLE\",\"remarks\":\"tzh测试\"}],\"paymentStatus\":\"PAYED\",\"status\":\"CANCEL\"}}}";
        message.setPayload(JSON.parseObject(json));
        message.setReplaceOnExist(false);
        message.setMaxRetry(3);
        Response response = messageClient.sendMessage(message);
        System.out.println(response);
    }

    @Test
    public void testXmlVersion() {
        sendMessage("A42B86551F554173854C047116B0DD97");
    }

    @Test
    public void testFormVersion() {
        sendMessage("FA9F8BC0882B40559C32C13625CA4592");
    }

    @Test
    public void testJsonVersion() {
        sendMessage("B5E023D5667D42179788DB563E062878");
    }



}

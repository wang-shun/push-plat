package com.lvmama;

import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.domain.dto.MessageDTO;
import com.lvmama.bms.core.json.JSON;
import com.lvmama.bms.pusher.MessagePusher;
import com.lvmama.bms.pusher.protocol.http.HttpPusher;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

public class TestMsgPusher {

    public static void main(String[] args) {

//        BasicConfigurator.configure();

        MessagePusher pusher = new MessagePusher();
        pusher.setClusterName("tnt_msg_cluster");
//        pusher.setRegistryAddress("zookeeper://127.0.0.1:2181");
        pusher.setRegistryAddress("zookeeper://10.200.6.197:2181");
        pusher.setFastWorkThreads(10);
        pusher.setSlowWorkThreads(10);
        pusher.addConfig(ExtConfig.REDIS_ADDRESS, "10.200.6.197");
        pusher.addConfig(ExtConfig.REDIS_PORT, "6379");
        pusher.addConfig(ExtConfig.REDIS_NAME_SPACE, "tnt_bms");
        pusher.addConfig(ExtConfig.REDIS_MAX_TOTAL, "10");
        pusher.addConfig(ExtConfig.MQ_BROKER_URL, "tcp://10.200.6.197:61616");
        pusher.addConfig(ExtConfig.MQ_SYNC_TOPIC, "TNT_BMS_PUSHER_SYNC");

        pusher.start();

    }

    @Test
    public void testPush() {

        MessageDTO message = new MessageDTO();
        message.setToken("1001111");
        message.setConverterType(1);
        message.setPriority(100);
        message.setPushType(1);
        message.setPushUrl("https://www.baidu.com");
        message.setThrehold(10);

        Date date = new Date();
        String content = JSON.toJSONString(Arrays.asList(MessageDTO.class.getSimpleName(), message));
        message.setMsgContent(content);

        HttpPusher pusher = new HttpPusher(null);

        try {
            pusher.push(null);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testInetAddress() {
        try {
            InetAddress.getAllByName("www.baidu.com");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


}

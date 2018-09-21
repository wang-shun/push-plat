package com.lvmama.tnt.bms.test.service.impl;

import com.lvmama.bms.client.MessageClient;
import com.lvmama.bms.client.RetryMessageClient;
import com.lvmama.bms.client.domain.Response;
import com.lvmama.bms.core.domain.Message;

import java.util.List;

/**
 *
 */
public class MessageClientManager {

    private MessageClient messageClient;

//    public static String fangzhen = "zookeeper://192.168.0.208:2181,10.200.4.167:2181,10.200.4.181:2181";
    public static String ceshi = "zookeeper://10.200.6.197:2181";

    private MessageClientManager(){
        intMessageClient();
    }
    private void intMessageClient() {
        messageClient = new RetryMessageClient();
        //集群名称
        messageClient.setClusterName("tnt_msg_cluster");
        //设置zookeeper注册地址
        messageClient.setRegistryAddress(ceshi);
        //启动消息客户端
        messageClient.start();
    }

    public Response sendMessage(List<Message> messageList) {
        return messageClient.sendMessage(messageList);
    }

    public Response sendMessage(Message message) {
        return messageClient.sendMessage(message);
    }

    public static MessageClientManager getInstance() {
        return MySingletonHandler.instance;
    }

    //内部类
    private static class MySingletonHandler{
        private static MessageClientManager instance = new MessageClientManager();
    }
}

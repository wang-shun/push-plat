package com.lvmama;

import com.lvmama.bms.client.MessageClient;
import com.lvmama.bms.client.RetryMessageClient;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
public class MessageClientManager {

    private static Queue<MessageClient> clients = new ConcurrentLinkedQueue<MessageClient>();

    public static String fangzhen = "zookeeper://192.168.0.208:2181,10.200.4.167:2181,10.200.4.181:2181";
    public static String ceshi = "zookeeper://10.200.6.197:2181";

    public static MessageClient getInstance() {

        MessageClient messageClient = clients.poll();

        if(messageClient == null) {

            messageClient = new RetryMessageClient();
            //集群名称
            messageClient.setClusterName("tnt_bms_cluster");
            //设置zookeeper注册地址
            messageClient.setRegistryAddress(fangzhen);
            //启动消息客户端
            messageClient.start();
            clients.offer(messageClient);
        }

        return messageClient;
    }

    public static void release(MessageClient messageClient) {
        clients.offer(messageClient);
    }

}

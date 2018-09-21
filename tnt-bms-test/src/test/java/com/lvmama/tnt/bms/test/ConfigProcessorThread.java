package com.lvmama.tnt.bms.test;

import com.lvmama.tnt.bms.test.config.ZookeeperClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class ConfigProcessorThread implements Watcher{
    private static CountDownLatch countDownLatch=new CountDownLatch(1);
    private ZooKeeper  zooKeeper;

    public static void main(String[] args)throws Exception{
       ZookeeperClient client = new ZookeeperClient();
        if (client.startZkClient()) {
            client.setNodeData("/BMS/config/tnt_msg_cluster/MSG_SCHEDULER/bms.job.processor.thread", "200");
        }
        /*ConfigProcessorThread t = new ConfigProcessorThread();

        ZooKeeper  zooKeeper = new ZooKeeper("10.200.6.197:2181",3000,t);
        countDownLatch.await();

        byte[] byteData = zooKeeper.getData("/BMS/config/tnt_msg_cluster/MSG_SCHEDULER/bms.job.processor.thread",false,null);
        String data = new String(byteData, "utf-8");
        System.out.println(data);*/

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent.getState()== Event.KeeperState.SyncConnected){
            countDownLatch.countDown();
        }
    }
}

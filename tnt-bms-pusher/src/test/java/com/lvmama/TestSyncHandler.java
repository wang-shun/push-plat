package com.lvmama;

import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.bms.core.cluster.sync.SyncHandler;
import com.lvmama.bms.core.cluster.sync.SyncListener;
import com.lvmama.bms.core.constant.ExtConfig;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class TestSyncHandler {

    public static void main(String[] args) {

        try {
            Config config = new Config();
            config.setParameter(ExtConfig.MQ_BROKER_URL, "failover://(tcp://127.0.0.1:61616,tcp://10.200.4.83:61616)?randomize=false");
            config.setParameter(ExtConfig.MQ_SYNC_TOPIC, "TNT_BMS_SCHEDULER_SYNC");

            SyncHandler syncHandler = new SyncHandler(config);
            syncHandler.start();

            syncHandler.addListener(new SyncListener() {
                @Override
                public void onSyncEvent(SyncEvent syncEvent) {
                    System.out.println(syncEvent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            CountDownLatch latch = new CountDownLatch(10);
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void sendSyncMessage() {
        Config config = new Config();
        config.setParameter(ExtConfig.MQ_BROKER_URL, "failover://(tcp://127.0.0.1:61616,tcp://10.200.4.83:61616)?randomize=false");
        config.setParameter(ExtConfig.MQ_SYNC_TOPIC, "TNT_BMS_SCHEDULER_SYNC");

        SyncEvent syncEvent = new SyncEvent();
        syncEvent.setObjectType(SyncEvent.ObjectType.MSG_TYPE);
        syncEvent.setEventType(SyncEvent.EventType.Add);
        SyncHandler syncHandler = new SyncHandler(config);

        for(int i = 0; i < 20; i++) {
            syncHandler.sync(syncEvent);
        }


    }


}

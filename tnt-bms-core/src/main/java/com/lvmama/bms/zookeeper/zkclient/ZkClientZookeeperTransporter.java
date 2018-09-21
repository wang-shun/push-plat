package com.lvmama.bms.zookeeper.zkclient;

import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.zookeeper.ZkClient;
import com.lvmama.bms.zookeeper.ZookeeperTransporter;

public class ZkClientZookeeperTransporter implements ZookeeperTransporter {

    public ZkClient connect(Config config) {
        return new ZkClientZkClient(config);
    }

}

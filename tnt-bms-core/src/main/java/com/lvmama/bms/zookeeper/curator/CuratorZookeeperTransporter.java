package com.lvmama.bms.zookeeper.curator;

import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.zookeeper.ZkClient;
import com.lvmama.bms.zookeeper.ZookeeperTransporter;

public class CuratorZookeeperTransporter implements ZookeeperTransporter {

    public ZkClient connect(Config config) {
        return new CuratorZkClient(config);
    }

}
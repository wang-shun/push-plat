package com.lvmama.bms.zookeeper.lts;

import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.zookeeper.ZkClient;
import com.lvmama.bms.zookeeper.ZookeeperTransporter;

public class LtsZookeeperTransporter implements ZookeeperTransporter {

    public ZkClient connect(Config config) {
        return new LtsZkClient(config);
    }

}

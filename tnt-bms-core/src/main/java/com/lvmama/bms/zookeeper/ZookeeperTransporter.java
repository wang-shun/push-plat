package com.lvmama.bms.zookeeper;


import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.spi.SPI;

@SPI(key = ExtConfig.ZK_CLIENT_KEY, dftValue = "zkclient")
public interface ZookeeperTransporter {

    ZkClient connect(Config config);

}

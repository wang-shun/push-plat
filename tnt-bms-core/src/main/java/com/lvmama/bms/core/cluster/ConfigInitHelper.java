package com.lvmama.bms.core.cluster;

import com.lvmama.bms.core.constant.Environment;
import com.lvmama.config.ZooKeeperConfigInit;
import com.lvmama.config.common.Constant;

import java.util.Properties;

public class ConfigInitHelper {

    public static Properties getSweetProps(String zkAddress, String owner, String applicationName, boolean isWatch) throws Exception {

        Properties properties = new Properties();
        properties.put(Constant.ZOOKEEPER_CONFIG_ADDRESS, zkAddress);
        properties.put(Constant.ZOOKEEPER_CONFIG_OWNER, owner);
        properties.put(Constant.ZOOKEEPER_CONFIG_APPLICATION_NAME, applicationName);
        properties.put(Constant.ZOOKEEPER_CONFIG_WATCHER_ENABLED, isWatch);

        ZooKeeperConfigInit zooKeeperConfigInit = new ZooKeeperConfigInit();
        Properties sweetProps = zooKeeperConfigInit.getProperties(properties);

        return sweetProps;

    }

    public static String getEnv(String[] args) {

        for(String arg : args) {
            if(arg.startsWith("--spring.profiles.active=")) {
                String[] env = arg.split("=");
                if(env.length > 1) {
                    return env[1];
                }
            }
        }

        return Environment.ARK.name();

    }


}

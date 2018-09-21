package com.lvmama.bms.pusher.config;

import com.lvmama.bms.core.cluster.ConfigInitHelper;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.constant.Environment;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
public class MsgPusherLoader  {
    private static Logger logger = LoggerFactory.getLogger(MsgPusherLoader.class);
    private static final String ENVIRONMENT_CFG = "tnt-bms-pusher-env.properties";
    public static Properties PROPERTIES = new Properties();

    public static MsgPusherCfg load(String[] args) throws CfgException {

        ClassLoader classLoader = MsgPusherLoader.class.getClassLoader();

        String env = ConfigInitHelper.getEnv(args);
        String configFile = ENVIRONMENT_CFG.replace("env", env);

        try {
            PROPERTIES.load(new InputStreamReader(classLoader.getResourceAsStream(configFile), "utf-8"));
            Properties sweetProps = ConfigInitHelper.getSweetProps(PROPERTIES.getProperty("sweetAddress"), env,
                    "tnt-bms-pusher", false);
            PROPERTIES.putAll(sweetProps);
            logger.info("tip=the config is {}", PROPERTIES);
        } catch (Exception e) {
            throw new CfgException("tip=fail load config|file="+configFile+"|args="+Arrays.toString(args)+"|exception=", e);
        }

        MsgPusherCfg cfg = new MsgPusherCfg();
        String registryAddress = PROPERTIES.getProperty("registryAddress");
        if (StringUtils.isEmpty(registryAddress)) {
            throw new CfgException("registryAddress can not be null.");
        }
        cfg.setRegistryAddress(registryAddress);

        String clusterName = PROPERTIES.getProperty("clusterName");
        if (StringUtils.isEmpty(clusterName)) {
            throw new CfgException("clusterName can not be null.");
        }
        cfg.setClusterName(clusterName);

        String fastWorkThreads = PROPERTIES.getProperty("fastWorkThreads");
        if (StringUtils.isEmpty(clusterName)) {
            throw new CfgException("clusterName can not be null.");
        }
        cfg.setFastWorkThreads(Integer.parseInt(fastWorkThreads));

        String slowWorkThreads = PROPERTIES.getProperty("slowWorkThreads");
        if (StringUtils.isEmpty(clusterName)) {
            throw new CfgException("clusterName can not be null.");
        }
        cfg.setSlowWorkThreads(Integer.parseInt(slowWorkThreads));

        Map<String, String> configs = new HashMap<String, String>();
        for (Map.Entry<Object, Object> entry : PROPERTIES.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue() == null ? null : entry.getValue().toString();
            configs.put(key, value);
        }

        cfg.setConfig(configs);

        return cfg;
    }

}

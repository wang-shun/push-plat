package com.lvmama.bms.scheduler.config;

import com.lvmama.bms.core.cluster.ConfigInitHelper;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.constant.Environment;
import com.lvmama.config.ZooKeeperConfigInit;
import com.lvmama.config.common.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author Robert HG (254963746@qq.com) on 9/1/15.
 */
public class MsgSchedulerCfgLoader {

    private static Logger logger = LoggerFactory.getLogger(MsgSchedulerCfgLoader.class);
    private static final String ENVIRONMENT_CFG = "tnt-bms-scheduler-env.properties";

    public static Properties PROPERTIES = new Properties();

    public static MsgSchedulerCfg load(String[] args) throws CfgException {

        ClassLoader classLoader = MsgSchedulerCfgLoader.class.getClassLoader();

        String env = ConfigInitHelper.getEnv(args);
        String configFile = ENVIRONMENT_CFG.replace("env", env);

        try {
            PROPERTIES.load(new InputStreamReader(classLoader.getResourceAsStream(configFile), "utf-8"));
            Properties sweetProps = ConfigInitHelper.getSweetProps(PROPERTIES.getProperty("sweetAddress"),
                    env, "tnt-bms-scheduler", false);
            PROPERTIES.putAll(sweetProps);
            logger.info("tip=the config is {}", PROPERTIES);
        } catch (Exception e) {
            throw new CfgException("tip=fail load config|file="+configFile+"|args="+Arrays.toString(args)+"|exception=", e);
        }

        MsgSchedulerCfg cfg = new MsgSchedulerCfg();
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

        String bindIp = PROPERTIES.getProperty("bindIp");
        if (StringUtils.isNotEmpty(clusterName)) {
            cfg.setBindIp(bindIp);
        }

        String listenPort = PROPERTIES.getProperty("listenPort");
        if (StringUtils.isEmpty(listenPort) || !StringUtils.isInteger(listenPort)) {
            throw new CfgException("listenPort can not be null.");
        }
        cfg.setListenPort(Integer.parseInt(listenPort));

        Map<String, String> configs = new HashMap<String, String>();
        for (Map.Entry<Object, Object> entry : PROPERTIES.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue() == null ? null : entry.getValue().toString();
            configs.put(key, value);
        }

        cfg.setConfigs(configs);

        return cfg;
    }

}

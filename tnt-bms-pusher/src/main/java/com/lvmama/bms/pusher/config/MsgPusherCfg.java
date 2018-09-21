package com.lvmama.bms.pusher.config;

import java.util.Map;

/**
 *
 */
public class MsgPusherCfg {
    /**
     * 集群名称
     */
    private String clusterName;
    /**
     * 注册地址
     */
    private String registryAddress;
    /**
     * 快线程数
     */
    private int fastWorkThreads;
    /**
     * 慢线程数
     */
    private int slowWorkThreads;

    private Map<String, String> config;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public int getFastWorkThreads() {
        return fastWorkThreads;
    }

    public void setFastWorkThreads(int fastWorkThreads) {
        this.fastWorkThreads = fastWorkThreads;
    }

    public int getSlowWorkThreads() {
        return slowWorkThreads;
    }

    public void setSlowWorkThreads(int slowWorkThreads) {
        this.slowWorkThreads = slowWorkThreads;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
}

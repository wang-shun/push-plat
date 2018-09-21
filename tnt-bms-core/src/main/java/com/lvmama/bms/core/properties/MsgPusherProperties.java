package com.lvmama.bms.core.properties;


import com.lvmama.bms.autoconfigure.annotation.ConfigurationProperties;
import com.lvmama.bms.core.cluster.AbstractConfigProperties;
import com.lvmama.bms.core.commons.utils.Assert;
import com.lvmama.bms.core.constant.Level;
import com.lvmama.bms.core.exception.ConfigPropertiesIllegalException;

/**
 * @author Robert HG (254963746@qq.com) on 4/9/16.
 */
@ConfigurationProperties(prefix = "lts.tasktracker")
public class MsgPusherProperties extends AbstractConfigProperties {

    /**
     * 节点Group
     */
    private String nodeGroup;
    /**
     * FailStore数据存储路径
     */
    private String dataPath;

    /**
     * 工作线程,默认64
     */
    private int fastWorkThreads;

    private int slowWorkThreads;



    private Level bizLoggerLevel;

    private DispatchRunner dispatchRunner;

    private Class<?> jobRunnerClass;

    public String getNodeGroup() {
        return nodeGroup;
    }

    public void setNodeGroup(String nodeGroup) {
        this.nodeGroup = nodeGroup;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
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

    public Class<?> getJobRunnerClass() {
        return jobRunnerClass;
    }

    public void setJobRunnerClass(Class<?> jobRunnerClass) {
        this.jobRunnerClass = jobRunnerClass;
    }

    public Level getBizLoggerLevel() {
        return bizLoggerLevel;
    }

    public void setBizLoggerLevel(Level bizLoggerLevel) {
        this.bizLoggerLevel = bizLoggerLevel;
    }

    public DispatchRunner getDispatchRunner() {
        return dispatchRunner;
    }

    public void setDispatchRunner(DispatchRunner dispatchRunner) {
        this.dispatchRunner = dispatchRunner;
    }

    @Override
    public void checkProperties() throws ConfigPropertiesIllegalException {
        Assert.hasText(getClusterName(), "clusterName must have value.");
        Assert.hasText(getNodeGroup(), "nodeGroup must have value.");
        Assert.hasText(getRegistryAddress(), "registryAddress must have value.");
        Assert.isTrue(getFastWorkThreads() >= 0, "workThreads must >= 0.");
        Assert.isTrue(getSlowWorkThreads() >= 0, "workThreads must >= 0.");

    }

    public static class DispatchRunner {
        /**
         * 是否使用shardRunner
         */
        private boolean enable = false;
        /**
         * shard的字段,默认taskId
         */
        private String shardValue;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getShardValue() {
            return shardValue;
        }

        public void setShardValue(String shardValue) {
            this.shardValue = shardValue;
        }
    }
}

package com.lvmama.bms.core.properties;


import com.lvmama.bms.autoconfigure.annotation.ConfigurationProperties;
import com.lvmama.bms.core.cluster.AbstractConfigProperties;
import com.lvmama.bms.core.exception.ConfigPropertiesIllegalException;
import com.lvmama.bms.core.commons.utils.Assert;

/**
 * @author Robert HG (254963746@qq.com) on 4/9/16.
 */
@ConfigurationProperties(prefix = "lts.jobclient")
public class MsgClientProperties extends AbstractConfigProperties {

    private String nodeGroup;
    private boolean useRetryClient = true;
    private String dataPath;

    public String getNodeGroup() {
        return nodeGroup;
    }

    public void setNodeGroup(String nodeGroup) {
        this.nodeGroup = nodeGroup;
    }

    public boolean isUseRetryClient() {
        return useRetryClient;
    }

    public void setUseRetryClient(boolean useRetryClient) {
        this.useRetryClient = useRetryClient;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    @Override
    public void checkProperties() throws ConfigPropertiesIllegalException {
        Assert.hasText(getClusterName(), "clusterName must have value.");
        Assert.hasText(getNodeGroup(), "nodeGroup must have value.");
        Assert.hasText(getRegistryAddress(), "registryAddress must have value.");
    }
}

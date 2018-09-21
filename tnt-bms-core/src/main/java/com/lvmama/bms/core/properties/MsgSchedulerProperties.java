package com.lvmama.bms.core.properties;


import com.lvmama.bms.autoconfigure.annotation.ConfigurationProperties;
import com.lvmama.bms.core.cluster.AbstractConfigProperties;
import com.lvmama.bms.core.commons.utils.Assert;
import com.lvmama.bms.core.exception.ConfigPropertiesIllegalException;

/**
 * @author Robert HG (254963746@qq.com) on 4/9/16.
 */
@ConfigurationProperties(prefix = "bms.scheduler")
public class MsgSchedulerProperties extends AbstractConfigProperties {

    /**
     * 监听端口
     */
    private Integer listenPort;

    public Integer getListenPort() {
        return listenPort;
    }

    public void setListenPort(Integer listenPort) {
        this.listenPort = listenPort;
    }

    @Override
    public void checkProperties() throws ConfigPropertiesIllegalException {
        Assert.hasText(getClusterName(), "clusterName must have value.");
        Assert.hasText(getRegistryAddress(), "registryAddress must have value.");
    }
}

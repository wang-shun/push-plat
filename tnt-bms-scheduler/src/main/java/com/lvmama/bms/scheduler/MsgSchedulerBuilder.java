package com.lvmama.bms.scheduler;

import com.lvmama.bms.autoconfigure.PropertiesConfigurationFactory;
import com.lvmama.bms.core.cluster.AbstractNodeBuilder;
import com.lvmama.bms.core.commons.utils.CollectionUtils;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.properties.MsgSchedulerProperties;

import java.util.Map;

/**
 * @author Robert HG (254963746@qq.com) on 4/21/16.
 */
public class MsgSchedulerBuilder extends AbstractNodeBuilder<MessageScheduler, MsgSchedulerBuilder> {

    @Override
    protected MessageScheduler build0() {
        MsgSchedulerProperties properties = PropertiesConfigurationFactory.createPropertiesConfiguration(MsgSchedulerProperties.class, locations);
        return buildByProperties(properties);
    }

    public static MessageScheduler buildByProperties(MsgSchedulerProperties properties) {

        properties.checkProperties();

        MessageScheduler scheduler = new MessageScheduler();
        scheduler.setRegistryAddress(properties.getRegistryAddress());
        if (StringUtils.isNotEmpty(properties.getClusterName())) {
            scheduler.setClusterName(properties.getClusterName());
        }
        if (properties.getListenPort() != null) {
            scheduler.setListenPort(properties.getListenPort());
        }
        if (StringUtils.isNotEmpty(properties.getIdentity())) {
            scheduler.setIdentity(properties.getIdentity());
        }
        if (StringUtils.isNotEmpty(properties.getBindIp())) {
            scheduler.setBindIp(properties.getBindIp());
        }
        if (CollectionUtils.isNotEmpty(properties.getConfigs())) {
            for (Map.Entry<String, String> entry : properties.getConfigs().entrySet()) {
                scheduler.addConfig(entry.getKey(), entry.getValue());
            }
        }
        return scheduler;
    }
}

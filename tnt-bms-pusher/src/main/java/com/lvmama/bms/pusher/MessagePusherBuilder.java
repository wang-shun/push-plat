package com.lvmama.bms.pusher;

import com.lvmama.bms.autoconfigure.PropertiesConfigurationFactory;
import com.lvmama.bms.core.cluster.AbstractNodeBuilder;
import com.lvmama.bms.core.commons.utils.CollectionUtils;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.properties.MsgPusherProperties;
import com.lvmama.bms.pusher.runner.PushRunner;

import java.util.Map;

/**
 * @author Robert HG (254963746@qq.com) on 4/21/16.
 */
public class MessagePusherBuilder extends AbstractNodeBuilder<MessagePusher, MessagePusherBuilder> {

    @Override
    protected MessagePusher build0() {

        MsgPusherProperties properties = PropertiesConfigurationFactory.createPropertiesConfiguration(MsgPusherProperties.class, locations);
        return buildByProperties(properties);
    }

    @SuppressWarnings("unchecked")
    public static MessagePusher buildByProperties(MsgPusherProperties properties) {
        MessagePusher messagePusher = new MessagePusher();
        messagePusher.setRegistryAddress(properties.getRegistryAddress());
        if (StringUtils.isNotEmpty(properties.getClusterName())) {
            messagePusher.setClusterName(properties.getClusterName());
        }
        if (StringUtils.isNotEmpty(properties.getIdentity())) {
            messagePusher.setIdentity(properties.getIdentity());
        }
        if (StringUtils.isNotEmpty(properties.getNodeGroup())) {
            messagePusher.setNodeGroup(properties.getNodeGroup());
        }
        if (StringUtils.isNotEmpty(properties.getDataPath())) {
            messagePusher.setDataPath(properties.getDataPath());
        }
        if (StringUtils.isNotEmpty(properties.getBindIp())) {
            messagePusher.setBindIp(properties.getBindIp());
        }
        if (CollectionUtils.isNotEmpty(properties.getConfigs())) {
            for (Map.Entry<String, String> entry : properties.getConfigs().entrySet()) {
                messagePusher.addConfig(entry.getKey(), entry.getValue());
            }
        }
        if (properties.getBizLoggerLevel() != null) {
            messagePusher.setBizLoggerLevel(properties.getBizLoggerLevel());
        }

        return messagePusher;
    }
}

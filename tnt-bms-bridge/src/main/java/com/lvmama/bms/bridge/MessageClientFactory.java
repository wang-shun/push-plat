package com.lvmama.bms.bridge;

import com.lvmama.bms.client.MessageClient;
import com.lvmama.bms.client.RetryMessageClient;

public class MessageClientFactory {

    private static MessageClient messageClient;

    public MessageClientFactory(String registryAddress, String clusterName, Boolean isRetry) {

        messageClient = isRetry? new RetryMessageClient() : new MessageClient();
        messageClient.setRegistryAddress(registryAddress.toLowerCase().startsWith("zookeeper")?
                registryAddress : "zookeeper://".concat(registryAddress));
        messageClient.setClusterName(clusterName);
        messageClient.start();

    }

    public static MessageClient getInstance() {
        return messageClient;
    }

}

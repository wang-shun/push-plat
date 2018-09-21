package com.lvmama.bms.core.cluster;

import com.lvmama.bms.core.constant.Constants;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.factory.NamedThreadFactory;
import com.lvmama.bms.core.registry.RegistryFactory;
import com.lvmama.bms.core.remoting.HeartBeatMonitor;
import com.lvmama.bms.core.remoting.RemotingClientDelegate;
import com.lvmama.bms.core.spi.ServiceLoader;
import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.remoting.RemotingClient;
import com.lvmama.bms.remoting.RemotingClientConfig;
import com.lvmama.bms.remoting.RemotingProcessor;
import com.lvmama.bms.remoting.RemotingTransporter;

import java.util.concurrent.Executors;

/**
 * @author Robert HG (254963746@qq.com) on 8/18/14.
 *         抽象客户端
 */
public abstract class AbstractClientNode<T extends Node, Context extends AppContext> extends AbstractMsgNode<T, Context> {

    protected RemotingClientDelegate remotingClient;
    private HeartBeatMonitor heartBeatMonitor;

    protected void remotingStart() {
        remotingClient.start();
        heartBeatMonitor.start();

        RemotingProcessor defaultProcessor = getDefaultProcessor();
        if (defaultProcessor != null) {
            int processorSize = config.getParameter(ExtConfig.PROCESSOR_THREAD, Constants.DEFAULT_PROCESSOR_THREAD);
            remotingClient.registerDefaultProcessor(defaultProcessor,
                    Executors.newFixedThreadPool(processorSize,
                            new NamedThreadFactory(AbstractClientNode.class.getSimpleName(), true)));
        }
    }

    /**
     * 得到默认的处理器
     */
    protected abstract RemotingProcessor getDefaultProcessor();
    
    protected void remotingStop() {
        heartBeatMonitor.stop();
        remotingClient.shutdown();
    }

    /**
     * 设置节点组名
     */
    public void setNodeGroup(String nodeGroup) {
        config.setNodeGroup(nodeGroup);
    }

    public boolean isServerEnable() {
        return remotingClient.isServerEnable();
    }

    @Override
    protected void beforeRemotingStart() throws Exception {
        //
        this.remotingClient = new RemotingClientDelegate(getRemotingClient(new RemotingClientConfig()), appContext);
        this.heartBeatMonitor = new HeartBeatMonitor(remotingClient, appContext);
        
        registry = RegistryFactory.getRegistry(appContext);
        appContext.setRegistry(registry);

        beforeStart();
    }

    private RemotingClient getRemotingClient(RemotingClientConfig remotingClientConfig) {
        return ServiceLoader.load(RemotingTransporter.class, config).getRemotingClient(appContext, remotingClientConfig);
    }

    @Override
    protected void afterRemotingStart() {
        // 父类要做的
        afterStart();
    }

    @Override
    protected void beforeRemotingStop() {
        beforeStop();
    }

    @Override
    protected void afterRemotingStop() {
        afterStop();
    }

    protected abstract void beforeStart() throws Exception;

    protected abstract void afterStart();

    protected abstract void afterStop();

    protected abstract void beforeStop();

}

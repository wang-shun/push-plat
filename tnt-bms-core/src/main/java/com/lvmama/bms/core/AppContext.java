package com.lvmama.bms.core;

import com.lvmama.bms.cmd.HttpCmdServer;
import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.cluster.MasterElector;
import com.lvmama.bms.core.cluster.SubscribedNodeManager;
import com.lvmama.bms.core.monitor.MStatReporter;
import com.lvmama.bms.core.protocol.command.CommandBodyWrapper;
import com.lvmama.bms.core.registry.Registry;
import com.lvmama.bms.core.registry.RegistryStatMonitor;
import com.lvmama.bms.ec.EventCenter;

/**
 * @author Robert HG (254963746@qq.com) on 8/17/14.
 *         用来存储 程序的数据
 */
public abstract class AppContext {

    // 节点配置信息
    private Config config;
    // 节点管理
    private SubscribedNodeManager subscribedNodeManager;
    // master节点选举者
    private MasterElector masterElector;
    // 节点通信CommandBody包装器
    private CommandBodyWrapper commandBodyWrapper;
    // 事件中心
    private EventCenter eventCenter;
    // 监控中心
    private MStatReporter mStatReporter;
    // 注册中心状态监控
    private RegistryStatMonitor registryStatMonitor;
    // 命令中心
    private HttpCmdServer httpCmdServer;
    // 配置中心
    private Registry registry;

    public MStatReporter getMStatReporter() {
        return mStatReporter;
    }

    public void setMStatReporter(MStatReporter mStatReporter) {
        this.mStatReporter = mStatReporter;
    }

    public EventCenter getEventCenter() {
        return eventCenter;
    }

    public void setEventCenter(EventCenter eventCenter) {
        this.eventCenter = eventCenter;
    }

    public CommandBodyWrapper getCommandBodyWrapper() {
        return commandBodyWrapper;
    }

    public void setCommandBodyWrapper(CommandBodyWrapper commandBodyWrapper) {
        this.commandBodyWrapper = commandBodyWrapper;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public SubscribedNodeManager getSubscribedNodeManager() {
        return subscribedNodeManager;
    }

    public void setSubscribedNodeManager(SubscribedNodeManager subscribedNodeManager) {
        this.subscribedNodeManager = subscribedNodeManager;
    }

    public MasterElector getMasterElector() {
        return masterElector;
    }

    public void setMasterElector(MasterElector masterElector) {
        this.masterElector = masterElector;
    }

    public RegistryStatMonitor getRegistryStatMonitor() {
        return registryStatMonitor;
    }

    public void setRegistryStatMonitor(RegistryStatMonitor registryStatMonitor) {
        this.registryStatMonitor = registryStatMonitor;
    }

    public HttpCmdServer getHttpCmdServer() {
        return httpCmdServer;
    }

    public void setHttpCmdServer(HttpCmdServer httpCmdServer) {
        this.httpCmdServer = httpCmdServer;
    }

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
    
}

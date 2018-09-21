package com.lvmama.bms.core.registry;

import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.cluster.Node;
import com.lvmama.bms.zookeeper.DataListener;

/**
 * @author Robert HG (254963746@qq.com) on 6/22/14.
 *         节点注册接口
 */
public interface Registry {

    /**
     * 节点注册
     */
    void register(Node node);

    /**
     * 节点 取消注册
     */
    void unregister(Node node);

    /**
     * 监听节点
     */
    void subscribe(Node node, NotifyListener listener);

    /**
     * 取消监听节点
     */
    void unsubscribe(Node node, NotifyListener listener);

    /**
     * 销毁
     */
    void destroy();

	void addConfig(String key, Object value);
	
	void deleteConfig(String key);
	
	void updateConfig(String key, Object newVal);
	
	<T> T getConfig(String key, T defaultVal);

	boolean existConfig(String key);
	
    void addListener(String path, DataListener listener);
	
	String getAbsolutePath(Config config, String path);
	
}

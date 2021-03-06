package com.lvmama.bms.core.listener;

import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.cluster.Node;
import com.lvmama.bms.core.cluster.NodeType;
import com.lvmama.bms.core.commons.utils.CollectionUtils;
import com.lvmama.bms.core.constant.EcTopic;
import com.lvmama.bms.ec.EventInfo;

import java.util.List;

/**
 * 用来监听自己的节点信息变化
 *
 * @author Robert HG (254963746@qq.com) on 5/11/15.
 */
public class SelfChangeListener implements NodeChangeListener {

    private Config config;
    private AppContext appContext;

    public SelfChangeListener(AppContext appContext) {
        this.config = appContext.getConfig();
        this.appContext = appContext;
    }


    private void change(Node node) {
        if (node.getIdentity().equals(config.getIdentity())) {
            // 是当前节点, 看看节点配置是否发生变化
            // 1. 看 threads 有没有改变 , 目前只有 TASK_TRACKER 对 threads起作用
            if (node.getNodeType().equals(NodeType.MSG_PUSHER)) {
                boolean isChange = false;
                if(node.getFastThreads()!=config.getFastWorkThreads()) {
                    config.setFastWorkThreads(node.getFastThreads());
                    isChange = true;
                }
                if(node.getSlowThreads()!=config.getSlowWorkThreads()) {
                    config.setSlowWorkThreads(node.getSlowThreads());
                    isChange = true;
                }
                if(isChange) {
                    appContext.getEventCenter().publishAsync(new EventInfo(EcTopic.WORK_THREAD_CHANGE));
                }
            }

            // 2. 看 available 有没有改变
            if (node.isAvailable() != config.isAvailable()) {
                String topic = node.isAvailable() ? EcTopic.NODE_ENABLE : EcTopic.NODE_DISABLE;
                config.setAvailable(node.isAvailable());
                appContext.getEventCenter().publishAsync(new EventInfo(topic));
            }
        }
    }

    @Override
    public void addNodes(List<Node> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return;
        }
        for (Node node : nodes) {
            change(node);
        }
    }

    @Override
    public void removeNodes(List<Node> nodes) {

    }
}

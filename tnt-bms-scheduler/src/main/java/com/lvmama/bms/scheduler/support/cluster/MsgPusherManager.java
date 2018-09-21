package com.lvmama.bms.scheduler.support.cluster;


import com.lvmama.bms.core.cluster.Node;
import com.lvmama.bms.core.cluster.NodeType;
import com.lvmama.bms.core.commons.concurrent.ConcurrentHashSet;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.scheduler.channel.ChannelWrapper;
import com.lvmama.bms.scheduler.domain.MsgPusherNode;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Robert HG (254963746@qq.com) on 8/16/14.
 *         Task Tracker 管理器 (对 TaskTracker 节点的记录 和 可用线程的记录)
 */
public class MsgPusherManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgPusherManager.class);
    // 单例
    private final ConcurrentHashMap<String/*nodeGroup*/, Set<MsgPusherNode>> NODE_MAP = new ConcurrentHashMap<String, Set<MsgPusherNode>>();
    private MsgSchedulerAppContext appContext;

    public MsgPusherManager(MsgSchedulerAppContext appContext) {
        this.appContext = appContext;
    }

    /**
     * get all connected node group
     */
    public Set<String> getNodeGroups() {
        return NODE_MAP.keySet();
    }

    /**
     * 添加节点
     */
    public void addNode(Node node) {
        //  channel 可能为 null
        ChannelWrapper channel = appContext.getChannelManager().getChannel(node.getGroup(),
                node.getNodeType(), node.getIdentity());
        Set<MsgPusherNode> taskTrackerNodes = NODE_MAP.get(node.getGroup());

        if (taskTrackerNodes == null) {
            taskTrackerNodes = new ConcurrentHashSet<MsgPusherNode>();
            Set<MsgPusherNode> oldSet = NODE_MAP.putIfAbsent(node.getGroup(), taskTrackerNodes);
            if (oldSet != null) {
                taskTrackerNodes = oldSet;
            }
        }

        MsgPusherNode taskTrackerNode = new MsgPusherNode(node.getGroup(),
                node.getFastThreads(), node.getSlowThreads(), node.getIdentity(), channel);
        LOGGER.info("Add TaskTracker node:{}", taskTrackerNode);
        taskTrackerNodes.add(taskTrackerNode);

//        appContext.getNodeGroupStore().addNodeGroup(NodeType.TASK_TRACKER, node.getGroup());
    }

    /**
     * 删除节点
     *
     * @param node
     */
    public void removeNode(Node node) {
        Set<MsgPusherNode> taskTrackerNodes = NODE_MAP.get(node.getGroup());
        if (taskTrackerNodes != null && taskTrackerNodes.size() != 0) {
            MsgPusherNode taskTrackerNode = new MsgPusherNode(node.getIdentity());
            taskTrackerNode.setNodeGroup(node.getGroup());
            LOGGER.info("Remove TaskTracker node:{}", taskTrackerNode);
            taskTrackerNodes.remove(taskTrackerNode);
        }
    }

    public MsgPusherNode getTaskTrackerNode(String nodeGroup, String identity) {
        Set<MsgPusherNode> taskTrackerNodes = NODE_MAP.get(nodeGroup);
        if (taskTrackerNodes == null || taskTrackerNodes.size() == 0) {
            return null;
        }

        for (MsgPusherNode taskTrackerNode : taskTrackerNodes) {
            if (taskTrackerNode.getIdentity().equals(identity)) {
                if (taskTrackerNode.getChannel() == null || taskTrackerNode.getChannel().isClosed()) {
                    // 如果 channel 已经关闭, 更新channel, 如果没有channel, 略过
                    ChannelWrapper channel = appContext.getChannelManager().getChannel(
                            taskTrackerNode.getNodeGroup(), NodeType.MSG_PUSHER, taskTrackerNode.getIdentity());
                    if (channel != null) {
                        // 更新channel
                        taskTrackerNode.setChannel(channel);
                        LOGGER.info("update node channel , taskTackerNode={}", taskTrackerNode);
                        return taskTrackerNode;
                    }
                } else {
                    // 只有当channel正常的时候才返回
                    return taskTrackerNode;
                }
            }
        }
        return null;
    }

    /**
     * 更新节点的 可用线程数
     * @param timestamp        时间戳, 只有当 时间戳大于上次更新的时间 才更新可用线程数
     */
    public void updateTaskTrackerAvailableThreads(
            String nodeGroup,
            String identity,
            Integer fastIdleThreads,
            Integer slowIdleThreads,
            Long timestamp) {

        Set<MsgPusherNode> taskTrackerNodes = NODE_MAP.get(nodeGroup);

        if (taskTrackerNodes != null && taskTrackerNodes.size() != 0) {
            for (MsgPusherNode trackerNode : taskTrackerNodes) {
                if (trackerNode.getIdentity().equals(identity) && (trackerNode.getTimestamp() == null || trackerNode.getTimestamp() <= timestamp)) {
                    trackerNode.setFastIdleThread(fastIdleThreads);
                    trackerNode.setSlowIdleThread(slowIdleThreads);
                    trackerNode.setTimestamp(timestamp);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("更新节点线程数: {}", trackerNode);
                    }
                }
            }
        }
    }
}

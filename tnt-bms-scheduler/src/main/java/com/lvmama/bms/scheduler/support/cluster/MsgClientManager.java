package com.lvmama.bms.scheduler.support.cluster;

import com.lvmama.bms.core.cluster.Node;
import com.lvmama.bms.core.cluster.NodeType;
import com.lvmama.bms.core.commons.concurrent.ConcurrentHashSet;
import com.lvmama.bms.core.commons.utils.CollectionUtils;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.loadbalance.LoadBalance;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.spi.ServiceLoader;
import com.lvmama.bms.scheduler.channel.ChannelWrapper;
import com.lvmama.bms.scheduler.domain.MsgClientNode;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Robert HG (254963746@qq.com) on 8/17/14.
 *         客户端节点管理
 */
public class MsgClientManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MsgClientManager.class);

    private final ConcurrentHashMap<String/*nodeGroup*/, Set<MsgClientNode>> NODE_MAP = new ConcurrentHashMap<String, Set<MsgClientNode>>();

    private LoadBalance loadBalance;
    private MsgSchedulerAppContext appContext;

    public MsgClientManager(MsgSchedulerAppContext appContext) {
        this.appContext = appContext;
        this.loadBalance = ServiceLoader.load(LoadBalance.class, appContext.getConfig(), ExtConfig.JOB_CLIENT_SELECT_LOADBALANCE);
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
        ChannelWrapper channel = appContext.getChannelManager().getChannel(node.getGroup(), node.getNodeType(), node.getIdentity());
        Set<MsgClientNode> jobClientNodes = NODE_MAP.get(node.getGroup());

        if (jobClientNodes == null) {
            jobClientNodes = new ConcurrentHashSet<MsgClientNode>();
            Set<MsgClientNode> oldSet = NODE_MAP.putIfAbsent(node.getGroup(), jobClientNodes);
            if (oldSet != null) {
                jobClientNodes = oldSet;
            }
        }

        MsgClientNode jobClientNode = new MsgClientNode(node.getGroup(), node.getIdentity(), channel);
        LOGGER.info("add JobClient node:{}", jobClientNode);
        jobClientNodes.add(jobClientNode);

//        appContext.getNodeGroupStore().addNodeGroup(NodeType.MSG_CLIENT, node.getGroup());
    }

    /**
     * 删除节点
     */
    public void removeNode(Node node) {
        Set<MsgClientNode> jobClientNodes = NODE_MAP.get(node.getGroup());
        if (jobClientNodes != null && jobClientNodes.size() != 0) {
            for (MsgClientNode jobClientNode : jobClientNodes) {
                if (node.getIdentity().equals(jobClientNode.getIdentity())) {
                    LOGGER.info("remove JobClient node:{}", jobClientNode);
                    jobClientNodes.remove(jobClientNode);
                }
            }
        }
    }

    /**
     * 得到 可用的 客户端节点
     */
    public MsgClientNode getAvailableJobClient(String nodeGroup) {

        Set<MsgClientNode> jobClientNodes = NODE_MAP.get(nodeGroup);

        if (CollectionUtils.isEmpty(jobClientNodes)) {
            return null;
        }

        List<MsgClientNode> list = new ArrayList<MsgClientNode>(jobClientNodes);

        while (list.size() > 0) {

            MsgClientNode jobClientNode = loadBalance.select(list, null);

            if (jobClientNode != null && (jobClientNode.getChannel() == null || jobClientNode.getChannel().isClosed())) {
                ChannelWrapper channel = appContext.getChannelManager().getChannel(jobClientNode.getNodeGroup(), NodeType.MSG_CLIENT, jobClientNode.getIdentity());
                if (channel != null) {
                    // 更新channel
                    jobClientNode.setChannel(channel);
                }
            }

            if (jobClientNode != null && jobClientNode.getChannel() != null && !jobClientNode.getChannel().isClosed()) {
                return jobClientNode;
            } else {
                list.remove(jobClientNode);
            }
        }
        return null;
    }

}

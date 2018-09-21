package com.lvmama.bms.scheduler.domain;

import com.lvmama.bms.core.cluster.Node;
import com.lvmama.bms.core.cluster.NodeType;

/**
 * @author Robert HG (254963746@qq.com) on 7/23/14.
 * Job Tracker 节点
 */
public class MsgSchedulerNode extends Node {

    public MsgSchedulerNode() {
        this.setNodeType(NodeType.MSG_SCHEDULER);
        this.addListenNodeType(NodeType.MSG_CLIENT);
        this.addListenNodeType(NodeType.MSG_PUSHER);
        this.addListenNodeType(NodeType.MSG_SCHEDULER);
        this.addListenNodeType(NodeType.MONITOR);
    }
}

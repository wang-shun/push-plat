package com.lvmama.bms.pusher.domain;


import com.lvmama.bms.core.cluster.Node;
import com.lvmama.bms.core.cluster.NodeType;

/**
 * @author Robert HG (254963746@qq.com) on 8/14/14.
 *         TaskTracker 节点
 */
public class MsgPusherNode extends Node {

    public MsgPusherNode() {
        this.setNodeType(NodeType.MSG_PUSHER);
        // 关注 JobTracker
        this.addListenNodeType(NodeType.MSG_SCHEDULER);
        this.addListenNodeType(NodeType.MSG_PUSHER);
        this.addListenNodeType(NodeType.MONITOR);
    }

}

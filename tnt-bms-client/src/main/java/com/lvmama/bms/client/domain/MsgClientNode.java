package com.lvmama.bms.client.domain;

import com.lvmama.bms.core.cluster.Node;
import com.lvmama.bms.core.cluster.NodeType;

/**
 * @author Robert HG (254963746@qq.com) on 7/25/14.
 *         任务客户端节点
 */
public class MsgClientNode extends Node {

    public MsgClientNode() {
        this.setNodeType(NodeType.MSG_CLIENT);
        this.addListenNodeType(NodeType.MSG_SCHEDULER);
        this.addListenNodeType(NodeType.MSG_PUSHER);
        this.addListenNodeType(NodeType.MONITOR);
    }

}

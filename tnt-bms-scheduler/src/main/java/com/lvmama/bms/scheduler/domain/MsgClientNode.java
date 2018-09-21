package com.lvmama.bms.scheduler.domain;

import com.lvmama.bms.scheduler.channel.ChannelWrapper;

/**
 * @author Robert HG (254963746@qq.com) on 8/17/14.
 * 客户端节点
 */
public class MsgClientNode {

    // 节点组名称
    public String nodeGroup;
    // 唯一标识
    public String identity;
    // 该节点的channel
    public ChannelWrapper channel;

    public MsgClientNode(String nodeGroup, String identity, ChannelWrapper channel) {
        this.nodeGroup = nodeGroup;
        this.identity = identity;
        this.channel = channel;
    }

    public MsgClientNode(String identity) {
        this.identity = identity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MsgClientNode)) return false;

        MsgClientNode that = (MsgClientNode) o;

        if (identity != null ? !identity.equals(that.identity) : that.identity != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return identity != null ? identity.hashCode() : 0;
    }

    public String getNodeGroup() {
        return nodeGroup;
    }

    public void setNodeGroup(String nodeGroup) {
        this.nodeGroup = nodeGroup;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public ChannelWrapper getChannel() {
        return channel;
    }

    public void setChannel(ChannelWrapper channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "MsgClientNode{" +
                "nodeGroup='" + nodeGroup + '\'' +
                ", identity='" + identity + '\'' +
                ", channel=" + channel +
                '}';
    }
}

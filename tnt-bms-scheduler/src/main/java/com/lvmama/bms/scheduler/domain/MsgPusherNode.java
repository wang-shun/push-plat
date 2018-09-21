package com.lvmama.bms.scheduler.domain;


import com.lvmama.bms.scheduler.channel.ChannelWrapper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * TaskTracker状态对象
 */
public class MsgPusherNode {
    // 节点组名称
    public String nodeGroup;
    // 可用线程数
    public AtomicInteger fastIdleThread;
    public AtomicInteger slowIdleThread;
    // 唯一标识
    public String identity;
    // 该节点的channel
    public ChannelWrapper channel;

    public Long timestamp = null;

    public MsgPusherNode(String nodeGroup, int fastIdleThread, int slowIdleThread, String identity, ChannelWrapper channel) {
        this.nodeGroup = nodeGroup;
        this.fastIdleThread = new AtomicInteger(fastIdleThread);
        this.slowIdleThread = new AtomicInteger(slowIdleThread);
        this.identity = identity;
        this.channel = channel;
    }

    public MsgPusherNode(String identity) {
        this.identity = identity;
    }

    public MsgPusherNode(String identity, String nodeGroup) {
        this.nodeGroup = nodeGroup;
        this.identity = identity;
    }

    public String getNodeGroup() {
        return nodeGroup;
    }

    public void setNodeGroup(String nodeGroup) {
        this.nodeGroup = nodeGroup;
    }

    public AtomicInteger getFastIdleThread() {
        return fastIdleThread;
    }

    public void setFastIdleThread(Integer fastIdleThread) {
        this.fastIdleThread = new AtomicInteger(fastIdleThread);
    }

    public AtomicInteger getSlowIdleThread() {
        return slowIdleThread;
    }

    public void setSlowIdleThread(Integer slowIdleThread) {
        this.slowIdleThread = new AtomicInteger(slowIdleThread);
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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MsgPusherNode)) return false;

        MsgPusherNode that = (MsgPusherNode) o;

        if (identity != null ? !identity.equals(that.identity) : that.identity != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return identity != null ? identity.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MsgPusherNode{" +
                "nodeGroup:'" + nodeGroup + '\'' +
                ", fastIdleThread:" + (fastIdleThread == null ? 0 : fastIdleThread.get()) +
                ", slowIdleThread:" + (slowIdleThread == null ? 0 : slowIdleThread.get()) +
                ", identity:'" + identity + '\'' +
                '}';
    }
}
package com.lvmama.bms.scheduler.support.listener;

import com.lvmama.bms.core.cluster.Node;
import com.lvmama.bms.core.cluster.NodeType;
import com.lvmama.bms.core.listener.NodeChangeListener;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.support.assign.TaskAssigner;

import java.util.List;

public class TaskAssignChangeListener implements NodeChangeListener {

    private MsgSchedulerAppContext appContext;

    public TaskAssignChangeListener(MsgSchedulerAppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public void addNodes(List<Node> nodes) {
        assign(nodes);
    }

    @Override
    public void removeNodes(List<Node> nodes) {
        assign(nodes);
    }

    private void assign(List<Node> nodes) {

        for(Node node : nodes) {
            if(NodeType.MSG_SCHEDULER.equals(node.getNodeType())) {
                TaskAssigner taskAssigner = appContext.getTaskAssigner();
                taskAssigner.assignTask();
                return;
            }
        }

    }

}

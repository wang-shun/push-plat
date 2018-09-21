package com.lvmama.bms.scheduler.support.listener;

import com.lvmama.bms.alarm.AlarmNotifier;
import com.lvmama.bms.alarm.email.EmailAlarmMessage;
import com.lvmama.bms.alarm.email.EmailTableUtils;
import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.cluster.Node;
import com.lvmama.bms.core.cluster.NodeType;
import com.lvmama.bms.core.commons.utils.CollectionUtils;
import com.lvmama.bms.core.commons.utils.DateUtils;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.listener.NodeChangeListener;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Robert HG (254963746@qq.com) on 8/17/14.
 *         节点变化监听器
 */
public class MsgNodeChangeListener implements NodeChangeListener {

    private Config config;
    private MsgSchedulerAppContext appContext;

    public MsgNodeChangeListener(MsgSchedulerAppContext appContext) {
        this.appContext = appContext;
        this.config = appContext.getConfig();
    }

    @Override
    public void addNodes(List<Node> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return;
        }
        for (Node node : nodes) {
            if (node.getNodeType().equals(NodeType.MSG_PUSHER)) {
                appContext.getMsgPusherManager().addNode(node);
            } else if (node.getNodeType().equals(NodeType.MSG_CLIENT)) {
                appContext.getMsgClientManager().addNode(node);
            }
        }
    }

    @Override
    public void removeNodes(List<Node> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return;
        }
        List<List<Object>> noticeInfo = new ArrayList<List<Object>>();
        for (Node node : nodes) {
            if (node.getNodeType().equals(NodeType.MSG_PUSHER)) {
                appContext.getMsgPusherManager().removeNode(node);
                noticeInfo.add(buildMessage(node));
            } else if (node.getNodeType().equals(NodeType.MSG_CLIENT)) {
                appContext.getMsgClientManager().removeNode(node);
            }
        }

        if (CollectionUtils.isNotEmpty(noticeInfo)) {
            notifyNodeOffline(noticeInfo);
        }
    }

    private List<Object> buildMessage(Node node) {
        Object[] message = {node.getClusterName(),node.getNodeType().name(),node.getGroup(),
                node.getHostName(),node.getIp(),node.getPort(),node.getHttpCmdPort(),String.valueOf(node.isAvailable()),
                DateUtils.formatDate(node.getCreateTime(), DateUtils.YMD_HMS)};
        return Arrays.asList(message);
    }

    private void notifyNodeOffline(List<List<Object>> nifos) {
        EmailAlarmMessage alarmMessage = new EmailAlarmMessage();
        alarmMessage.setTitle(config.getParameter(ExtConfig.EMAIL_NODE_OFFLINE_TITLE));
        alarmMessage.setTo(config.getParameter(ExtConfig.ALARM_EMAIL_TO));
        String message = EmailTableUtils.createTable(config.getParameter(ExtConfig.EMAIL_NODE_OFFLINE_TABLE_TITLE),
                config.getParameter(ExtConfig.EMAIL_NODE_OFFLINE_TABLE_HEADER).split(","),nifos);
        alarmMessage.setMsg(message);
        getAlarmNotifier().notice(alarmMessage);
    }

    public AlarmNotifier<EmailAlarmMessage> getAlarmNotifier() {
        return appContext.getAlarmNotifier();
    }

}

package com.lvmama.bms.scheduler.support.assign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lvmama.bms.core.cluster.Node;
import com.lvmama.bms.core.cluster.NodeType;
import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.bms.core.cluster.sync.SyncHandler;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import com.lvmama.bms.scheduler.store.MsgTypeStatisStore;
import com.lvmama.bms.scheduler.store.domain.po.MsgTypePO;
import com.lvmama.bms.scheduler.store.domain.po.statis.MsgTypeStatisPO;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TaskAssigner {

    private Logger logger = LoggerFactory.getLogger(TaskAssigner.class);

    private MsgSchedulerAppContext appContext;

    private String localNodeIdentity;

    private SyncHandler syncHandler;

    private Map<TaskType, Map<TaskNode, List<Task>>> assignments = new ConcurrentHashMap<>();

    private ReentrantLock lock = new ReentrantLock();

    private ScheduledExecutorService checkTimer;

    public TaskAssigner(MsgSchedulerAppContext appContext, String identity) {
        this.appContext = appContext;
        this.localNodeIdentity = identity;
        this.syncHandler = appContext.getSyncHandler();
        checkTimer = Executors.newScheduledThreadPool(1);
        checkTimer.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                checkConsistent();
            }
        }, 120, 30, TimeUnit.SECONDS);
    }

    public void assignTask() {
        if(appContext.isMaster()) {
            lock.lock();
            try {
                assignPreloadTask();
                syncTaskAssign();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     *  分配消息预加载任务到节点
     */
    private void assignPreloadTask() {

        List<Task> tasks = new ArrayList<>();
        Map<Integer, Double> taskWeights = calculatePreloadTaskWeight();

        double totalOfWeight = 0;
        for(Double weight : taskWeights.values()) {
            totalOfWeight+=weight;
        }
        double averageOfWeight = totalOfWeight != 0 ? totalOfWeight / taskWeights.size() : 1;

        Set<MsgTypePO> msgTypes = appContext.getMsgCacheManager().getUnAssignMsgTypeOn();
        for(MsgTypePO msgType : msgTypes) {
            Double weight = taskWeights.get(msgType.getId());
            Task task = new Task(msgType.getType(), weight != null ? weight : averageOfWeight, msgType);
            tasks.add(task);
        }

        List<TaskNode> taskNodes = new ArrayList<>();
        List<Node> nodeList = appContext.getSubscribedNodeManager().getNodeList(NodeType.MSG_SCHEDULER);
        for (Node node : nodeList) {
            TaskNode taskNode = new TaskNode(node.getIdentity(), 1d);
            taskNodes.add(taskNode);
        }

        this.assignments.put(TaskType.MsgPreload, divideTaskEqually(taskNodes, tasks));

        logger.info("tip=task assign|assignments="+JSON.toJSONString(this.assignments));

    }

    private Map<Integer, Double> calculatePreloadTaskWeight() {

        Map<Integer, Double> taskWeights = new HashMap<>();

        MsgTypeStatisStore msgTypeStatisStore = appContext.getMsgTypeStatisStore();

        Calendar current = Calendar.getInstance();
        current.set(Calendar.HOUR_OF_DAY, 0);
        current.set(Calendar.MINUTE, 0);
        current.set(Calendar.SECOND, 0);
        current.set(Calendar.MILLISECOND, 0);

        Date before = current.getTime();
        current.add(Calendar.DAY_OF_MONTH, -1);
        Date after = current.getTime();

        List<MsgTypeStatisPO> msgTypeStatisList = msgTypeStatisStore.sum(after, before);

        double total = 0d;
        for(MsgTypeStatisPO msgTypeStatis : msgTypeStatisList) {
            total+= msgTypeStatis.getSendCount();
        }

        for(MsgTypeStatisPO msgTypeStatis : msgTypeStatisList) {
            Double weight = msgTypeStatis.getSendCount() / total;
            taskWeights.put(msgTypeStatis.getMsgTypeId(), weight);
        }

        return taskWeights;

    }

    /**
     * 根据权重将Task均分到TaskNode->数据均分
     * @param taskNodes
     * @param tasks
     * @return
     */
    private Map<TaskNode, List<Task>> divideTaskEqually(List<TaskNode> taskNodes, List<Task> tasks) {

        Map<TaskNode, List<Task>> assignment = new HashMap<>();

        if(taskNodes.size() > 1) {
            List<List<Task>> dataGroups = Anneal.calculate(tasks, taskNodes.size(), 100);
            logger.info("tip=task assign|anneal="+JSON.toJSONString(dataGroups));
            for(int i = 0; i < taskNodes.size(); i++) {
                assignment.put(taskNodes.get(i), dataGroups.get(i));
            }
        } else if(taskNodes.size() > 0) {
            assignment.put(taskNodes.get(0), tasks);
        }

        return assignment;

    }

    private void syncTaskAssign() {

        SyncEvent syncEvent = new SyncEvent();
        syncEvent.setObjectType(SyncEvent.ObjectType.TASK_ASSIGN);
        syncEvent.setEventType(SyncEvent.EventType.Reload);
        syncEvent.setAddition(JSON.toJSONString(assignments));
        syncHandler.sync(syncEvent);

    }

    public void flushTaskAssign(String taskAssign) {

        try {
            Map<TaskType, Map<TaskNode, List<Task>>> assignments = JSON.parseObject(taskAssign, new TypeReference<Map<TaskType, Map<TaskNode, List<Task>>>>(){});
            for(Iterator<Map<TaskNode, List<Task>>> iterator4Assignments = assignments.values().iterator(); iterator4Assignments.hasNext();) {
                Map<TaskNode, List<Task>> assignment = iterator4Assignments.next();
                for(Iterator<List<Task>> iterator4Assignment = assignment.values().iterator(); iterator4Assignment.hasNext();) {
                    for(Task task : iterator4Assignment.next()) {
                        task.setAttachment(JSON.toJavaObject((JSON) task.getAttachment(), Class.forName(task.getAttachType())));
                    }
                }
            }
            this.assignments = assignments;
            logger.info("tip=flush task assign|assignments="+JSON.toJSONString(this.assignments));
        } catch (ClassNotFoundException ignored) {
        }

    }

    private void checkConsistent() {
        checkConsistent4Preload();
    }

    private void checkConsistent4Preload() {

        List<Node> nodeList = appContext.getSubscribedNodeManager().getNodeList(NodeType.MSG_SCHEDULER);
        if(nodeList != null && nodeList.size() != assignments.get(TaskType.MsgPreload).size()) {
            logger.info("tip=task assign find not consistent node size");
            assignTask();
        }

        Set<MsgTypePO> msgTypes = appContext.getMsgCacheManager().getUnAssignMsgTypeOn();
        Map<TaskNode, List<Task>> assignment = assignments.get(TaskType.MsgPreload);
        if(assignment != null) {
            int count = 0;
            for(Iterator<List<Task>> iterator = assignment.values().iterator(); iterator.hasNext();) {
                count+=iterator.next().size();
            }
            if(count!=msgTypes.size()) {
                logger.info("tip=task assign find not consistent msgType size");
                assignTask();
            }
        }

    }

    public <T> Set<T> acquireTask(TaskType taskType) {

        Set<T> result = new HashSet<>();

        Map<TaskNode, List<Task>> assignment = assignments.get(taskType);
        if(assignment == null) {
            return result;
        }

        List<Task> taskList = assignment.get(new TaskNode(localNodeIdentity));
        if(taskList == null) {
            return result;
        }

        for(Task task : taskList) {
            result.add((T) task.getAttachment());
        }

        return result;

    }

}

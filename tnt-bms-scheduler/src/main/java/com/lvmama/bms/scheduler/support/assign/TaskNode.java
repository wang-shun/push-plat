package com.lvmama.bms.scheduler.support.assign;

class TaskNode {

    private String uid;

    private Double weight;

    public TaskNode() {
    }

    public TaskNode(String uid) {
        this.uid = uid;
    }

    public TaskNode(String uid, Double weight) {
        this.uid = uid;
        this.weight = weight;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskNode taskNode = (TaskNode) o;

        return uid != null ? uid.equals(taskNode.uid) : taskNode.uid == null;
    }

    @Override
    public int hashCode() {
        return uid != null ? uid.hashCode() : 0;
    }

}

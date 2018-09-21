package com.lvmama.bms.scheduler.support.assign;

import com.lvmama.bms.core.commons.utils.StringUtils;

class Task implements AnnealData {

    private String uid;

    private Double weight;

    private Object attachment;

    private String attachType;

    public Task() {
    }

    public Task(String uid, Double weight, Object attachment) {
        this.uid = uid;
        this.weight = weight;
        this.attachment = attachment;
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

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

    public String getAttachType() {
        if(StringUtils.isNotEmpty(attachType)) {
            return attachType;
        }
        return attachment.getClass().getName();
    }

    public void setAttachType(String attachType) {
        this.attachType = attachType;
    }

    @Override
    public double getData() {
        return weight;
    }

    @Override
    public String toString() {
        return weight.toString();
    }
}

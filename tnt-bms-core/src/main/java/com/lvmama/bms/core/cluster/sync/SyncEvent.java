package com.lvmama.bms.core.cluster.sync;

import java.io.Serializable;

public class SyncEvent implements Serializable{

    public SyncEvent() {
    }

    public SyncEvent(ObjectType objectType, EventType eventType, String addition) {
        this.objectType = objectType;
        this.eventType = eventType;
        this.addition = addition;
    }

    public enum ObjectType {
        MSG_TYPE,
        ACCESS,
        SLOW_MSG,
        TOKEN_SWITCH,
        MSG_PUSHER,
        MSG_CONVERTER,
        MANUAL_PUSH,
        FLUSH_REDIS,
        CONFIG,
        TASK_ASSIGN
    }

    public enum EventType {
        Add,
        Update,
        Delete,
        TurnOn,
        TurnOff,
        Reload,
        Assign,
        All
    }

    private ObjectType objectType;

    private EventType eventType;

    private String addition;

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    @Override
    public String toString() {
        return  "objectType=" + objectType +
                "|eventType=" + eventType +
                "|addition=" + addition;
    }
}

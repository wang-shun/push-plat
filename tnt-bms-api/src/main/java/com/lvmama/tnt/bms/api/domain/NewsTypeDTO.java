package com.lvmama.tnt.bms.api.domain;

import java.io.Serializable;

/**
 * 消息类型
 */
public class NewsTypeDTO implements Serializable{

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 消息类型名称 不能重复
     */
    private String type;
    /**
     * 消息类型优先级
     */
    private Integer priority;
    /**
     * 消息类型开启状态 1：开启 0：关闭
     */
    private Integer opened;

    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getOpened() {
        return opened;
    }

    public void setOpened(Integer opened) {
        this.opened = opened;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "NewsTypeDTO{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", priority=" + priority +
                ", opened=" + opened +
                ", version=" + version +
                '}';
    }
}

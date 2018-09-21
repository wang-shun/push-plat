package com.lvmama.tnt.bms.api.domain;

import java.io.Serializable;

/**
 * 映射器
 */
public class ConvertDTO implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 映射器名称
     */
    private String name;
    /**
     * 映射器内容
     */
    private String map;
    /**
     * 数据版本
     */
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ConvertDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", map='" + map + '\'' +
                ", version=" + version +
                '}';
    }
}

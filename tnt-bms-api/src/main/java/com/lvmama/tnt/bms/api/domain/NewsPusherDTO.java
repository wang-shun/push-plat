package com.lvmama.tnt.bms.api.domain;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 推送器
 */
public class NewsPusherDTO implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 推送器名称
     */
    private String name;
    /**
     * RPC jar名称
     */
    private String fileName;
    /**
     * jar 文件
     */
    private byte[] jar;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getJar() {
        return jar;
    }

    public void setJar(byte[] jar) {
        this.jar = jar;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "NewsPusherDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fileName='" + fileName + '\'' +
                ", jar=" + Arrays.toString(jar) +
                ", version=" + version +
                '}';
    }
}

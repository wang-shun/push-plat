package com.lvmama.tnt.bms.admin.web.domain.po;

import java.io.Serializable;

/**
 *
 */
public class MsgPusherDO implements Serializable{

    private static final long serialVersionUID = -8862687402581752988L;

    private Integer id;
    private String name;
    private String fileName;
    private byte[] jar;
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
}

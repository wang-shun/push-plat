package com.lvmama.bms.pusher.store.domain;

public class MsgPusherPO {

    private Integer id;

    private String name;

    private byte[] jar;

    private int version;

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

    public byte[] getJar() {
        return jar;
    }

    public void setJar(byte[] jar) {
        this.jar = jar;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "MsgPusherPO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version=" + version +
                '}';
    }
}

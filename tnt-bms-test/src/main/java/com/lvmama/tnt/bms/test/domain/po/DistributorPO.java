package com.lvmama.tnt.bms.test.domain.po;

/**
 *
 */
public class DistributorPO {
    private int id;
    private String name;
    private String token;
    private int convertType;
    private int convertMapID;

    public int getConvertType() {
        return convertType;
    }

    public void setConvertType(int convertType) {
        this.convertType = convertType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getConvertMapID() {
        return convertMapID;
    }

    public void setConvertMapID(int convertMapID) {
        this.convertMapID = convertMapID;
    }

    @Override
    public String toString() {
        return "DistributorPO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", convertMapID='" + convertMapID + '\'' +
                '}';
    }
}

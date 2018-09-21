package com.lvmama.tnt.bms.test.domain.vo;

/**
 *
 */
public class RegistVO {
    private int converterType;//1：json，2：xml，3：form
    private String name;
    private int priority;//1~10
    private int pushType = 1;
    private int opened = 1;
    private String pushUrl;
    private int receiveType = 1;//1
    private int threshold = 500;
    private String convertMap;

    public int getOpened() {
        return opened;
    }

    public void setOpened(int opened) {
        this.opened = opened;
    }

    public int getConverterType() {
        return converterType;
    }

    public void setConverterType(int converterType) {
        this.converterType = converterType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public int getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(int receiveType) {
        this.receiveType = receiveType;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public String getConvertMap() {
        return convertMap;
    }

    public void setConvertMap(String convertMap) {
        this.convertMap = convertMap;
    }
}

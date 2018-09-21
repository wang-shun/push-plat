package com.lvmama.bms.pusher.map.v1.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "bind")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataBind {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private boolean isAttr;

    @XmlAttribute
    private String wrapName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAttr() {
        return isAttr;
    }

    public void setAttr(boolean attr) {
        isAttr = attr;
    }

    public String getWrapName() {
        return wrapName;
    }

    public void setWrapName(String wrapName) {
        this.wrapName = wrapName;
    }
}

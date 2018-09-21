package com.lvmama.bms.pusher.map.v1.vo;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="type")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataType {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private boolean isAutoMap;

    private DataBind bind;

    @XmlElement(name= "prop")
    private List<DataProp> properties;

    @XmlTransient
    private boolean isRoot = true;

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAutoMap() {
        return isAutoMap;
    }

    public void setAutoMap(boolean autoMap) {
        isAutoMap = autoMap;
    }

    public DataBind getBind() {
        return bind;
    }

    public String getName() {
        return name;
    }

    public void setBind(DataBind bind) {
        this.bind = bind;
    }

    public List<DataProp> getProperties() {
        return properties;
    }

    public void setProperties(List<DataProp> properties) {
        this.properties = properties;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }
}

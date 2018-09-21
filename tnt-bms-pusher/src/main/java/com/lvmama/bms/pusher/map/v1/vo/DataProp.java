package com.lvmama.bms.pusher.map.v1.vo;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "prop")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataProp {

    @XmlAttribute
    private String name;

    @XmlAttribute
    private String type;

    @XmlAttribute
    private boolean isMulti;

    private DataBind bind;

    @XmlTransient
    private DataType dataType;

    @XmlElement(name = "script")
    private DataScript dataScript;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isMulti() {
        return isMulti;
    }

    public void setMulti(boolean multi) {
        isMulti = multi;
    }

    public DataBind getBind() {
        return bind;
    }

    public void setBind(DataBind bind) {
        this.bind = bind;
    }

    public DataScript getDataScript() {
        return dataScript;
    }

    public void setDataScript(DataScript dataScript) {
        this.dataScript = dataScript;
    }
}

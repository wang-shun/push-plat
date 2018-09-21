package com.lvmama.bms.pusher.map.v1.vo;

import com.lvmama.bms.pusher.map.vo.ResponseMap;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "mapping")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataMap {

    @XmlElement(name = "type")
    private List<DataType> dataTypes;

    @XmlTransient
    private DataType rootType;

    public List<DataType> getDataTypes() {
        return dataTypes;
    }

    public void setDataTypes(List<DataType> dataTypes) {
        this.dataTypes = dataTypes;
    }

    public DataType getRootType() {
        return rootType;
    }

    public void setRootType(DataType rootType) {
        this.rootType = rootType;
    }

}

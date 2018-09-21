package com.lvmama.bms.pusher.map.vo;

import com.lvmama.bms.core.commons.utils.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseMap {

    public enum  DecodeType {
        Status,
        XML,
        JSON,
        NotDefine
    }

    @XmlAttribute(name = "type")
    private String decodeType;

    @XmlAttribute(name = "path")
    private String path;

    @XmlAttribute(name = "value")
    private String value;

    public DecodeType getDecodeType() {

        if(StringUtils.isNotEmpty(decodeType)) {
            for(DecodeType decodeType : DecodeType.values()) {
                if(decodeType.name().toLowerCase().equals(this.decodeType.toLowerCase())) {
                    return decodeType;
                }
            }
        }

        return DecodeType.NotDefine;

    }
    public void setDecodeType(String decodeType)
 {
        this.decodeType = decodeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

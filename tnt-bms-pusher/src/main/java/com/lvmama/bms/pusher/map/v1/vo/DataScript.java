package com.lvmama.bms.pusher.map.v1.vo;

import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "script")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataScript {

    @XmlAttribute(name = "isEncrypt")
    private boolean isEncrypt;

    @XmlPath("text()")
    private String script;

    public boolean isEncrypt() {
        return isEncrypt;
    }

    public void setEncrypt(boolean encrypt) {
        isEncrypt = encrypt;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}

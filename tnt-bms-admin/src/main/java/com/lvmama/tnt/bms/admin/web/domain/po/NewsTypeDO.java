package com.lvmama.tnt.bms.admin.web.domain.po;


/**
 * @author longhr
 * @version 2017/11/1 0001
 */
public class NewsTypeDO extends BaseDO {

    private static final long serialVersionUID = 3586124207070332414L;

    private String type;
    private Integer priority;
    private Integer opened;
    private Integer version;

    public NewsTypeDO(){}

    public NewsTypeDO(String type) {
        this.type = type;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getOpened() {
        return opened;
    }

    public void setOpened(Integer opened) {
        this.opened = opened;
    }
}

package com.lvmama.tnt.bms.admin.web.manage.domain;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class BasePO implements Serializable {
    private static final long serialVersionUID = -1892149599629162245L;

    private Integer id;
    private String notes;
    private String active;
    private Date createTime;
    private Date modifyTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}

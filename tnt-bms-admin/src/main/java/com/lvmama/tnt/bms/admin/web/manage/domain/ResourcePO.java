package com.lvmama.tnt.bms.admin.web.manage.domain;


import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ResourcePO extends BasePO {
    private static final long serialVersionUID = -325754276851615269L;

    private String resCode;
    private String parentCode;
    private String index;
    private String title;
    private String path;
    private String icon;
    private Integer level;
    private Integer type;
    private String leafFlag;
    private ResourcePO parentResource;
    private List<ResourcePO> children = new ArrayList<>();

    public String getLeafFlag() {
        return leafFlag;
    }

    public void setLeafFlag(String leafFlag) {
        this.leafFlag = leafFlag;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ResourcePO getParentResource() {
        return parentResource;
    }

    public void setParentResource(ResourcePO parentResource) {
        this.parentResource = parentResource;
    }

    public List<ResourcePO> getChildren() {
        return children;
    }

    public void setChildren(List<ResourcePO> children) {
        this.children = children;
    }
}

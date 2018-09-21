package com.lvmama.tnt.bms.admin.web.manage.domain;

import java.util.List;

/**
 *
 **/
public class ResourceNode {
    private Integer id;
    private String code;
    private String label;
    private String isLeaf;
    private List<ResourceNode> children;
    private String permissions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ResourceNode> getChildren() {
        return children;
    }

    public void setChildren(List<ResourceNode> children) {
        this.children = children;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }
}

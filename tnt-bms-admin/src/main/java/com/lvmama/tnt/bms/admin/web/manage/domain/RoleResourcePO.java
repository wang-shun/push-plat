package com.lvmama.tnt.bms.admin.web.manage.domain;

/**
 *
 */
public class RoleResourcePO {
    private Integer roleId;
    private Integer resourceId;
    private String permissions;

    public RoleResourcePO() {
    }

    public RoleResourcePO(Integer roleId, Integer resourceId, String permissions) {
        this.roleId = roleId;
        this.resourceId = resourceId;
        this.permissions = permissions;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}

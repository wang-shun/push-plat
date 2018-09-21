package com.lvmama.tnt.bms.admin.web.manage.domain;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class RoleResourceDTO implements Serializable {

    private static final long serialVersionUID = -4385250263195484452L;
    private Integer roleId;
    private List<Integer> resourceIds;
    private List<String> permissions;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<Integer> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<Integer> resourceIds) {
        this.resourceIds = resourceIds;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}

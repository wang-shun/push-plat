package com.lvmama.tnt.bms.admin.web.manage.domain;


/**
 *
 */
public class RolePO extends BasePO {
    private static final long serialVersionUID = 7888953088364381562L;

    private String roleCode;
    private String roleName;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

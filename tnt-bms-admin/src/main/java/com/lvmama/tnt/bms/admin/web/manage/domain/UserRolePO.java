package com.lvmama.tnt.bms.admin.web.manage.domain;


/**
 *
 */
public class UserRolePO extends BasePO {
    private static final long serialVersionUID = -6219769322502466146L;

    private Integer userId;
    private Integer roleId;

    public UserRolePO() {
    }

    public UserRolePO(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}

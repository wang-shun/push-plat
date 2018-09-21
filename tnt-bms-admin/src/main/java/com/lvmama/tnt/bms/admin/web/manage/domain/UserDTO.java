package com.lvmama.tnt.bms.admin.web.manage.domain;

import com.lvmama.bms.core.commons.utils.StringUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class UserDTO implements Serializable{
    private static final long serialVersionUID = 4072981367687424928L;
    private Integer id;
    private String userName;
    private String password;
    private String salt;
    private String nickName;
    private String phone;
    private String email;
    private Set<String> roleCodes = new HashSet<>();
    private Set<String> accessUris = new HashSet<>();
    private Set<String> accessButtons = new HashSet<>();
    private Set<ResourceDTO> resourceDTOS = new HashSet<>();
    private List<Integer> roleIds;

    public void handleAccessUri() {
        if (this.resourceDTOS.size() > 0) {
            for (ResourceDTO dto : this.resourceDTOS) {
                this.accessUris.add(dto.getIndex());
                if (StringUtils.isNotEmpty(dto.getPermissions())) {
                    String[] arr = dto.getPermissions().split(",");
                    for (String p : arr) {
                        this.accessButtons.add(dto.getIndex() + "_" + p);
                    }
                 }
            }
        }
    }

    public Set<String> getAccessButtons() {
        return accessButtons;
    }

    public void setAccessButtons(Set<String> accessButtons) {
        this.accessButtons = accessButtons;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public Set<ResourceDTO> getResourceDTOS() {
        return resourceDTOS;
    }

    public void setResourceDTOS(Set<ResourceDTO> resourceDTOS) {
        this.resourceDTOS = resourceDTOS;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoleCodes() {
        return roleCodes;
    }

    public void setRoleCodes(Set<String> roleCodes) {
        this.roleCodes = roleCodes;
    }

    public Set<String> getAccessUris() {
        return accessUris;
    }

    public void setAccessUris(Set<String> accessUris) {
        this.accessUris = accessUris;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

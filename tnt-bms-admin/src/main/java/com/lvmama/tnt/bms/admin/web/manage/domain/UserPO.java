package com.lvmama.tnt.bms.admin.web.manage.domain;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class UserPO extends BasePO {
    private static final long serialVersionUID = 891593461019475211L;

    private String userName;
    private String password;
    private String salt;
    private String nickName;
    private String phone;
    private String email;
    private Set<String> roleCodes = new HashSet<>();
    private Set<String> accessUris = new HashSet<>();

    public Set<String> getAccessUris() {
        return accessUris;
    }

    public void setAccessUris(Set<String> accessUris) {
        this.accessUris = accessUris;
    }

    public String getCredentialsSalt() {
        return userName;
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

    public void setPassword(String password) {
        this.password = password;
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
}

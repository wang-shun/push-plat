package com.lvmama.bms.scheduler.store.domain.dto;

public class TokenSwitch {

    private String token;

    private int recoverTime;

    public TokenSwitch() {
    }

    public TokenSwitch(String token, int recoverTime) {
        this.token = token;
        this.recoverTime = recoverTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getRecoverTime() {
        return recoverTime;
    }

    public void setRecoverTime(int recoverTime) {
        this.recoverTime = recoverTime;
    }
}

package com.lvmama.bms.core.domain.enums;

public enum ConvertVersion {

    V1(1),
    V2(2),
    NotDefine(-1);

    private Integer value;

    ConvertVersion(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}

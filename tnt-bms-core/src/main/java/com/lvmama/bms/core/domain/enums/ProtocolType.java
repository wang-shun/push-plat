package com.lvmama.bms.core.domain.enums;

/**
 * 推送类型
 */
public enum ProtocolType {

    Http(1), TCP(2), RPC(3);

    private Integer value;

    ProtocolType(Integer value) {
        this.value = value;
    }

    public ProtocolType getProtocolTypeByValue(Integer value) {

        for(ProtocolType type : values()) {
            if(type.value.equals(value)) {
                return type;
            }
        }

        return null;

    }

    public Integer getValue() {
        return value;
    }
}

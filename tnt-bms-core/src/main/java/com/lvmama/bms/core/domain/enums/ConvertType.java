package com.lvmama.bms.core.domain.enums;

/**
 * 转换器类型
 */
public enum ConvertType {
    Json("application/json", 1),
    Xml("application/xml", 2),
    Form("application/x-www-form-urlencoded", 3);

    private String mimeType;
    private Integer value;

    ConvertType(String mimeType, Integer value) {
        this.mimeType = mimeType;
        this.value = value;
    }

    public String mimeType() {
        return this.mimeType;
    }

    public Integer value() {
        return value;
    }

    public static ConvertType getTypeByValue(Integer value) {
        if(value!=null) {
            for(ConvertType type : values()) {
                if(type.value.equals(value)) {
                    return type;
                }
            }
        }
        return null;
    }
}

package com.lvmama.bms.core.domain.enums;

import com.lvmama.bms.core.commons.utils.StringUtils;

public enum EncryptMethod {
    MD5;
    public static EncryptMethod getEnumByValue(String value) {
        if(StringUtils.isNotEmpty(value)) {
            for(EncryptMethod method : values()) {
                if(method.name().equals(value)) {
                    return method;
                }
            }
        }
        return null;
    }

}

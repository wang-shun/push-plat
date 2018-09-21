package com.lvmama.bms.pusher.convert;

import com.lvmama.bms.pusher.map.MapperContext;

import java.util.Map;

public interface MessageConverter {

    Map<String, String> formatHeader(MapperContext mapperContext) throws Exception;

    /**
     * MapperContext.payload数据类型:
     * string
     * number
     * jsonObject
     * array
     * true
     * false
     */
    <T> T formatMessage(MapperContext mapperContext) throws Exception;

}

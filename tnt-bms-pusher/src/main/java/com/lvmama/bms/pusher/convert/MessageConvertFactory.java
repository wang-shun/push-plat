package com.lvmama.bms.pusher.convert;

import com.lvmama.bms.core.domain.enums.ConvertType;
import com.lvmama.bms.pusher.convert.impl.FormMessageConverter;
import com.lvmama.bms.pusher.convert.impl.JsonMessageConverter;
import com.lvmama.bms.pusher.convert.impl.XmlMessageConverter;

import java.util.HashMap;
import java.util.Map;

public class MessageConvertFactory {

    private static Map<String, MessageConverter> messageConverterMap = new HashMap<String, MessageConverter>();

    static {
        messageConverterMap.put(ConvertType.Json.name(), new JsonMessageConverter());
        messageConverterMap.put(ConvertType.Xml.name(), new XmlMessageConverter());
        messageConverterMap.put(ConvertType.Form.name(), new FormMessageConverter());
    }

    public static MessageConverter getMessageConverter(String convertType) {
        return messageConverterMap.get(convertType);
    }


}

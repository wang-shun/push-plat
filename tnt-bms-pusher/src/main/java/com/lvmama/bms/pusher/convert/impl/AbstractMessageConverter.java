package com.lvmama.bms.pusher.convert.impl;

import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.pusher.convert.MessageConverter;
import com.lvmama.bms.pusher.map.MapperContext;
import ognl.Ognl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public abstract class AbstractMessageConverter implements MessageConverter {

    @Override
    public Map<String, String> formatHeader(MapperContext mapperContext) throws Exception {
        String headerTemplate = mapperContext.getHttpHeader();
        if (StringUtils.isEmpty(headerTemplate)) {
            return null;
        }
        Map<String, Object> root = new HashMap<>();
        root.put("payload", mapperContext.getPayload());
        Matcher matcher = Pattern.compile("<script.*?>(.*?)</script>", Pattern.DOTALL).matcher(headerTemplate);
        StringBuffer result = new StringBuffer();
        while(matcher.find()) {
            String scriptValue = StringUtils.toString(Ognl.getValue(matcher.group(1), root), true);
            matcher.appendReplacement(result, String.valueOf(scriptValue));
        }
        matcher.appendTail(result);
        Map<String, String> returnMap = new HashMap<>();
        String[] headers = Pattern.compile("&").split(result);
        for (String header : headers) {
            if (header.indexOf("=") > 0) {
                String[] temp = header.split("=",2);
                returnMap.put(temp[0], temp[1]);
            }
        }
        return returnMap;
    }
}

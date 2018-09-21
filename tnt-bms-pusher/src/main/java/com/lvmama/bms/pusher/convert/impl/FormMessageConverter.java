package com.lvmama.bms.pusher.convert.impl;

import com.alibaba.fastjson.JSONObject;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.pusher.map.MapperContext;
import com.lvmama.bms.pusher.map.v1.impl.FormMapper;
import com.lvmama.bms.pusher.map.v2.impl.TemplateMapper;

import java.util.Iterator;

public class FormMessageConverter extends AbstractMessageConverter {

    @Override
    public String formatMessage(MapperContext mapperContext) throws Exception {

        switch (mapperContext.getConvertVersion()) {
            case V1:
                if(mapperContext.getDataMap() != null) {
                    FormMapper formMapper = new FormMapper();
                    return formMapper.map(mapperContext);
                }
            case V2:
                if(StringUtils.isNotEmpty(mapperContext.getRequestMap())) {
                    TemplateMapper templateMapper = new TemplateMapper();
                    return templateMapper.map(mapperContext);
                }
            default:
                StringBuilder body = new StringBuilder();
                JSONObject json = (JSONObject) mapperContext.getPayload();
                for(Iterator<String> iterator = json.keySet().iterator(); iterator.hasNext();) {
                    String name = iterator.next();
                    body.append("&").append(name).append("=").append(json.get(name));
                }
                return body.length()==0 ? "" : body.substring(1);

        }

    }

}

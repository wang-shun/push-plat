package com.lvmama.bms.pusher.convert.impl;

import com.alibaba.fastjson.JSON;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.pusher.map.MapperContext;
import com.lvmama.bms.pusher.map.v1.impl.JsonMapper;
import com.lvmama.bms.pusher.map.v2.impl.TemplateMapper;

public class JsonMessageConverter extends AbstractMessageConverter {

    @Override
    public String formatMessage(MapperContext mapperContext) throws Exception {

        switch (mapperContext.getConvertVersion()) {
            case V1:
                if(mapperContext.getDataMap()!=null) {
                    JsonMapper jsonMapper = new JsonMapper();
                    return jsonMapper.map(mapperContext);
                }
            case V2:
                if(StringUtils.isNotEmpty(mapperContext.getRequestMap())) {
                    TemplateMapper templateMapper = new TemplateMapper();
                    return templateMapper.map(mapperContext);
                }
            default:
                return JSON.toJSONString(mapperContext.getPayload());
        }

    }

}

package com.lvmama.bms.pusher.map.v1.impl;

import com.alibaba.fastjson.JSONObject;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.pusher.map.MapperContext;
import com.lvmama.bms.pusher.map.ObjectMapper;
import com.lvmama.bms.pusher.map.v1.vo.DataBind;
import com.lvmama.bms.pusher.map.v1.vo.DataProp;
import com.lvmama.bms.pusher.map.v1.vo.DataScript;
import ognl.OgnlException;

public class FormMapper extends ObjectMapper {

    public String map(MapperContext mapperContext) throws OgnlException {

        StringBuilder builder = new StringBuilder();
        JSONObject rawJson = (JSONObject) mapperContext.getPayload();

        for(DataProp dataProp : mapperContext.getDataMap().getRootType().getProperties()) {
            DataBind dataBind = dataProp.getBind();
            builder.append(dataBind.getName()).
                    append("=");
            DataScript dataScript = dataProp.getDataScript();
            if(dataScript != null) {
                String result = evalScriptEL(rawJson, dataScript.getScript(),
                        dataScript.isEncrypt(), mapperContext.getEncryptMethod(), mapperContext.getEncryptKey());
                builder.append(StringUtils.isNotEmpty(result) ? result : "");
            } else {
                String propValue = rawJson.getString(dataProp.getName());
                builder.append(StringUtils.isNotEmpty(propValue) ? propValue : "");
            }

            builder.append("&");
        }

        if(builder.length() > 0) {
            return builder.substring(0, builder.length()-1);
        } else {
            return "";
        }

    }

}

package com.lvmama.bms.pusher.map.v1.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lvmama.bms.pusher.map.MapperContext;
import com.lvmama.bms.pusher.map.ObjectMapper;
import com.lvmama.bms.pusher.map.v1.vo.DataBind;
import com.lvmama.bms.pusher.map.v1.vo.DataProp;
import com.lvmama.bms.pusher.map.v1.vo.DataScript;
import com.lvmama.bms.pusher.map.v1.vo.DataType;
import ognl.OgnlException;

import java.util.Iterator;

public class JsonMapper extends ObjectMapper {

    public String map(MapperContext mapperContext) throws OgnlException {
        return map((JSON) mapperContext.getPayload(), mapperContext.getDataMap().getRootType(), mapperContext).toJSONString();
    }

    private JSON map(JSON raw, DataType dataType, MapperContext mapperContext) throws OgnlException {

        if(raw instanceof JSONArray) { //判断是否为数组类型

            JSONArray rawArray = (JSONArray) raw;
            JSONArray targetArray = new JSONArray();

            for(Iterator<Object> iterator = rawArray.iterator(); iterator.hasNext();){
                JSONObject rawJson = (JSONObject) iterator.next();
                JSONObject targetJson = toJson(rawJson, dataType, mapperContext);
                targetArray.add(targetJson);
            }

            return targetArray;

        } else {
            JSONObject rawJson = (JSONObject) raw;
            JSONObject targetJson = toJson(rawJson, dataType, mapperContext);
            return targetJson;
        }

    }

    private JSONObject toJson(JSONObject rawJson, DataType dataType, MapperContext mapperContext) throws OgnlException {

        JSONObject targetJson = new JSONObject();

        for(DataProp property : dataType.getProperties()) {

            DataType propType = property.getDataType();
            DataBind propBind = property.getBind();

            if(propType == null) {
                String propValue = rawJson.getString(property.getName());
                DataScript dataScript = property.getDataScript();
                if(dataScript!=null) {
                    String result = evalScriptEL((JSON) mapperContext.getPayload(), dataScript.getScript(),
                            dataScript.isEncrypt(), mapperContext.getEncryptMethod(), mapperContext.getEncryptKey());
                    targetJson.put(propBind.getName(), result);
                } else {
                    targetJson.put(propBind.getName(), propValue);
                }
            } else {
                if(property.isMulti()) {
                    JSONArray propJson = rawJson.getJSONArray(property.getName());
                    if(propJson!=null) {
                        targetJson.put(propBind.getName(), map(propJson, propType, mapperContext));
                    }
                } else {
                    JSONObject propJson = rawJson.getJSONObject(property.getName());
                    if(propJson!=null) {
                        targetJson.put(propBind.getName(), map(propJson, propType, mapperContext));
                    }
                }
            }
        }

        return targetJson;

    }

}

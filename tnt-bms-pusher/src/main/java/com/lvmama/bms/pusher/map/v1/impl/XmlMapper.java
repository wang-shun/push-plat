package com.lvmama.bms.pusher.map.v1.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.pusher.map.MapperContext;
import com.lvmama.bms.pusher.map.ObjectMapper;
import com.lvmama.bms.pusher.map.v1.vo.DataBind;
import com.lvmama.bms.pusher.map.v1.vo.DataProp;
import com.lvmama.bms.pusher.map.v1.vo.DataScript;
import com.lvmama.bms.pusher.map.v1.vo.DataType;
import ognl.OgnlException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import java.io.StringWriter;
import java.util.Iterator;

public class XmlMapper extends ObjectMapper {

    public String map(MapperContext mapperContext) throws Exception {

        DataType rootType = mapperContext.getDataMap().getRootType();
        Document document = DocumentHelper.createDocument();
        Element parent = document.addElement(rootType.getBind().getName());
        map((JSONObject) mapperContext.getPayload(), rootType, parent, mapperContext);

        StringWriter stringWriter = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(stringWriter);
        xmlWriter.write(document);

        return stringWriter.toString();

    }

    public void map(JSON raw, DataType dataType, Element parent, MapperContext mapperContext) throws OgnlException {

        JSONObject rawJson = (JSONObject) raw;

        for(DataProp dataProp : dataType.getProperties()) {
            DataBind dataBind = dataProp.getBind();
            if(StringUtils.isEmpty(dataProp.getType())) {
                if(dataBind.isAttr()) {
                    DataScript dataScript = dataProp.getDataScript();
                    if(dataScript!=null) {
                        String result = evalScriptEL((JSON) mapperContext.getPayload(), dataScript.getScript(),
                                dataScript.isEncrypt(), mapperContext.getEncryptMethod(), mapperContext.getEncryptKey());
                        parent.addAttribute(dataBind.getName(), result);

                    } else {
                        String propValue = rawJson.getString(dataProp.getName());
                        parent.addAttribute(dataBind.getName(), propValue);
                    }

                } else {
                    Element child = parent.addElement(dataBind.getName());
                    DataScript dataScript = dataProp.getDataScript();
                    if(dataScript!=null) {
                        String result = evalScriptEL((JSON) mapperContext.getPayload(), dataScript.getScript(),
                                dataScript.isEncrypt(), mapperContext.getEncryptMethod(), mapperContext.getEncryptKey());
                        child.addText(StringUtils.isNotEmpty(result)? result : "");
                    } else {
                        String text = rawJson.getString(dataProp.getName());
                        child.addText(StringUtils.isNotEmpty(text)? text : "");
                    }

                }
            } else {
                if(dataProp.isMulti()) {
                    JSONArray rawArray = rawJson.getJSONArray(dataProp.getName());
                    if(rawArray!=null && rawArray.size() > 0) {
                        Element wrap = parent;
                        if(StringUtils.isNotEmpty(dataBind.getWrapName())) {
                            wrap = parent.addElement(dataBind.getWrapName());
                        }
                        for(Iterator<Object> iterator = rawArray.iterator(); iterator.hasNext();) {
                            Element child = wrap.addElement(dataBind.getName());
                            map((JSON) iterator.next(), dataProp.getDataType(), child, mapperContext);
                        }
                    }
                } else {
                    JSONObject rawChildJson = rawJson.getJSONObject(dataProp.getName());
                    if(rawChildJson!=null) {
                        Element child = parent.addElement(dataBind.getName());
                        map(rawChildJson, dataProp.getDataType(), child, mapperContext);
                    }
                }
            }
        }
    }

}

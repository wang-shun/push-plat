package com.lvmama.bms.pusher.map.v2.impl;

import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.domain.enums.EncryptMethod;
import com.lvmama.bms.pusher.map.MapperContext;
import com.lvmama.bms.pusher.map.ObjectMapper;
import ognl.Ognl;
import ognl.OgnlException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateMapper extends ObjectMapper {

    public String map(MapperContext mapperContext) throws Exception {

        Map<String, Object> context = new HashMap<>();
        context.put("encryptKey", mapperContext.getEncryptKey());

        Map<String, Object> root = new HashMap<>();
        root.put("payload", mapperContext.getPayload());

        String result = evalForeachEL(mapperContext.getRequestMap(), context, root, mapperContext.getEncryptMethod());

        result = evalScriptEL(result, context, root, mapperContext.getEncryptMethod());

        return result;

    }

    private String evalForeachEL(String template, Map<String, Object> context, Object root, EncryptMethod encryptMethod) throws OgnlException, DocumentException {

        StringBuffer result = new StringBuffer();

        Matcher matcher = Pattern.compile("<foreach.*?>(.*?)</foreach>", Pattern.DOTALL).matcher(template);

        while(matcher.find()) {

            Document foreachDoc = DocumentHelper.parseText(matcher.group());
            Node separatorAttr = foreachDoc.selectSingleNode("/foreach/@separator");
            String separator = null;
            if(separatorAttr!=null) {
                separator = separatorAttr.getText();
            }

            Node collectionAttr = foreachDoc.selectSingleNode("/foreach/@collection");

            StringBuffer scriptResult = new StringBuffer();

            Object container = Ognl.getValue(collectionAttr.getText(), context, root);
            if(container instanceof Collection) {
                int index = 0;
                for(Object item : (Collection) container) {
                    Map<String, Object> itemContext = new HashMap<>();
                    itemContext.putAll(context);
                    itemContext.put("item", item);
                    scriptResult.append(evalScriptEL(matcher.group(1), itemContext, root, encryptMethod));
                    if(StringUtils.isNotEmpty(separator) && ++index!=((Collection) container).size()) {
                        scriptResult.append(separator);
                    }
                }
            } else if(container instanceof Array) {
                int length = Array.getLength(container);
                for(int index = 0; index < length; index++) {
                    Map<String, Object> itemContext = new HashMap<>();
                    itemContext.putAll(context);
                    itemContext.put("item", Array.get(container, index));
                    scriptResult.append(evalScriptEL(matcher.group(1), itemContext, root, encryptMethod));
                    if(StringUtils.isNotEmpty(separator) && index+1 != length) {
                        scriptResult.append(separator);
                    }
                }
            }

            matcher.appendReplacement(result, scriptResult.toString());

        }

        matcher.appendTail(result);

        return result.toString();

    }

    private String evalScriptEL(String template, Map<String, Object> context, Object root, EncryptMethod encryptMethod) throws OgnlException, DocumentException {

        StringBuffer result = new StringBuffer();

        Matcher matcher = Pattern.compile("<script.*?>(.*?)</script>", Pattern.DOTALL).matcher(template);
        while(matcher.find()) {
            boolean isEncrypt = false;
            Document scriptDoc = DocumentHelper.parseText(matcher.group());
            Node node = scriptDoc.selectSingleNode("/script/@isEncrypt");
            if(node!=null) {
                isEncrypt = Boolean.parseBoolean(node.getText());
            }
            Object scriptValue = super.evalScriptEL(root, context, matcher.group(1), isEncrypt, encryptMethod);
            matcher.appendReplacement(result, String.valueOf(scriptValue));
        }
        matcher.appendTail(result);

        return result.toString();

    }

}

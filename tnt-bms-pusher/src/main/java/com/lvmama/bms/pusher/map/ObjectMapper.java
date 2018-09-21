package com.lvmama.bms.pusher.map;

import com.alibaba.fastjson.JSON;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.domain.enums.EncryptMethod;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ObjectMapper {

    public abstract String map(MapperContext mapperContext) throws Exception;

    public String evalScriptEL(JSON raw, String script, boolean isEncrypt,
                               EncryptMethod encryptMethod, String encryptKey) throws OgnlException {

        Map<String, Object> context = new HashMap<>();
        if(encryptKey != null) {
            context.put("encryptKey", encryptKey);
        }

        Map<String, Object> root = new HashMap<>();
        root.put("payload", raw);

        return evalScriptEL(root, context, script, isEncrypt, encryptMethod);

    }

    protected String evalScriptEL(Object root, Map<String, Object> context,
                                  String script, boolean isEncrypt, EncryptMethod encryptMethod) throws OgnlException {

        script = evalConcatEL(root, context, script);
        String result = StringUtils.toString(Ognl.getValue(script, context, root), true);
        if(isEncrypt) {
            switch (encryptMethod) {
                case MD5:
                    result = DigestUtils.md5Hex(result);
                    break;
            }
        }

        return result;
    }

    /**
     *
     * @param root
     * @param script request.header.signed+','+request.body.order.orderId+','+request.body.order.policies[0]+concat(request.body.order.policies, item, item.name, ',')
     * @return
     */
    private String evalConcatEL(Object root, Map<String, Object> context, String script) throws OgnlException {

        StringBuffer result = new StringBuffer();

        //解析concat
        Matcher concatMatcher = Pattern.compile("concat\\((.*?)\\)").matcher(script);
        while(concatMatcher.find()) {
            String concatScript = concatMatcher.group(1);

            String[] params = concatScript.split(",");

            //解析分隔符
            String separator = null;
            Matcher separatorMatcher = Pattern.compile("'(.*)'").matcher(concatScript);
            if(separatorMatcher.find()) {
                separator = separatorMatcher.group(1);
            } else {
                throw new IllegalArgumentException("tip=can not find separator|concat="+concatScript);
            }

            StringBuilder concatResult = new StringBuilder();
            Collection collection = (Collection) Ognl.getValue(params[0].trim(), context, root);
            int index = 1;
            for(Object item : collection) {
                concatResult.append(Ognl.getValue(params[2].trim(), context, Collections.singletonMap(params[1].trim(), item)));
                if(index++ < collection.size()) {
                    concatResult.append(separator);
                }
            }

            concatMatcher.appendReplacement(result, "'"+concatResult.toString()+"'");

        }

        concatMatcher.appendTail(result);

        return result.toString();

    }

}

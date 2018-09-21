package com.lvmama;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lvmama.bms.core.domain.enums.EncryptMethod;
import com.lvmama.bms.pusher.map.MapperContext;
import com.lvmama.bms.pusher.map.v2.impl.TemplateMapper;
import ognl.OgnlException;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestTemplateMapper {

    @Test
    public void testJson() {

        try {
            JSONObject payload = new JSONObject();
            payload.put("changeType", "product");
            JSONArray prouducts = new JSONArray();
            for(int i = 0; i < 3; i++) {
                JSONObject product = new JSONObject();
                product.put("productId", "productId"+i);
                product.put("goodsId", "goodsId"+i);
                prouducts.add(product);
            }

            payload.put("products", prouducts);

            String requestMap = "\t{\n" +
                    "\t\n" +
                    "\t\t\"product\": {\n" +
                    "\t\t\t\t\"changeType\" : \"<script>payload.changeType</script>\",\n" +
                    "\t\t\t\t\"list\" : [\n" +
                    "\t\t\t\t\t<foreach collection=\"payload.products\" separator=\",\">\n" +
                    "\t\t\t\t\t\t{\n" +
                    "\t\t\t\t\t\t\t\"productId\": \"<script isEncrypt=\"false\">#item.productId+payload.changeType</script>\",\n" +
                    "\t\t\t\t\t\t\t\"goodsId\": \"<script isEncrypt=\"true\">#item.goodsId</script>\"\n" +
                    "\t\t\t\t\t\t}\n" +
                    "\t\t\t\t\t</foreach>\n" +
                    "\t\t\t\t]\n" +
                    "\t\t\t}\n" +
                    "\t}";

            MapperContext mapperContext = new MapperContext();
            mapperContext.setPayload(payload);
            mapperContext.setEncryptMethod(EncryptMethod.MD5);
            mapperContext.setEncryptKey("lisi");
            mapperContext.setRequestMap(requestMap);

            TemplateMapper templateMapper = new TemplateMapper();
            String result = templateMapper.map(mapperContext);

            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testXml() {

        try {

            JSONObject payload = new JSONObject();
            payload.put("changeType", "product");
            JSONArray prouducts = new JSONArray();
            for(int i = 0; i < 3; i++) {
                JSONObject product = new JSONObject();
                product.put("productId", "productId"+i);
                product.put("goodsId", "goodsId"+i);
                prouducts.add(product);
            }

            payload.put("products", prouducts);

            String requestMap = "\t<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
                    "\t<request>\n" +
                    "\t\t<header> \n" +
                    "\t\t</header>\n" +
                    "\t\t<body>\n" +
                    "\t\t\t<changeType><script>payload.changeType</script></changeType>\n" +
                            "<productIdsA><script>concat(payload.products, item, item.productId, '|')+#encryptKey</script></productIdsA>" +
                    "\t\t\t<foreach   collection=\"payload.products\">\n" +
                    "\t\t\t\t<product>\n" +
                    "\t\t\t\t\t<productId><script isEncrypt=\"false\">#item.productId+payload.changeType</script></productId>\n" +
                    "\t\t\t\t\t<goodsId><script isEncrypt=\"true\">#item.goodsId</script></goodsId>\n" +
                            "<productIdsB><script>concat(payload.products, item, item.productId, '|')+#encryptKey</script></productIdsB>"+
                    "\t\t\t\t</product>\n" +
                    "\t\t\t</foreach>\n" +
                    "\t\t</body>\n" +
                    "\t</request>";

            MapperContext mapperContext = new MapperContext();
            mapperContext.setPayload(payload);
            mapperContext.setEncryptMethod(EncryptMethod.MD5);
            mapperContext.setEncryptKey("lisi");
            mapperContext.setRequestMap(requestMap);

            TemplateMapper templateMapper = new TemplateMapper();
            String result = templateMapper.map(mapperContext);

            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testPattern() {
        Pattern pattern = Pattern.compile("<foreach collection=(.*?)>(.*?)</foreach>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher("<foreach collection=\"payload.products\">\nssss\t\t\t</foreach>\n");
        while(matcher.find()) {
            System.out.println(matcher.group());
        }
    }


}

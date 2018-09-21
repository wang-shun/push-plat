package com.lvmama;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lvmama.bms.core.commons.utils.JAXBUtils;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.domain.enums.EncryptMethod;
import com.lvmama.bms.pusher.map.v1.RequestMapBuilder;
import com.lvmama.bms.pusher.map.MapperContext;
import com.lvmama.bms.pusher.map.v1.impl.FormMapper;
import com.lvmama.bms.pusher.map.v1.impl.JsonMapper;
import com.lvmama.bms.pusher.map.v1.impl.XmlMapper;
import com.lvmama.bms.pusher.map.v1.vo.DataMap;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestMapper {

    @Test
    public void testHeader() {
        try {
            String template = "PartnerId=123,Authorization=adfaegagda,Date=Fri Jun 22 11:45:46 CST 2018";//"PartnerId=<script>payload.header.PartnerId</script>,Authorization=<script>payload.header.Authorization</script>,Date=<script>payload.header.Date</script>";
            Map<String, Object> header = new HashMap<>();
            header.put("PartnerId", "123");
            header.put("Authorization", "adfaegagda");
            header.put("Date", new Date());

            Map<String,Object> payload = new HashMap<>();
            payload.put("header", header);

            Map<String,Object> root = new HashMap<>();
            root.put("payload", payload);

            JSONObject obj = JSON.parseObject(JSON.toJSONString(payload));
            Matcher matcher = Pattern.compile("<script.*?>(.*?)</script>", Pattern.DOTALL).matcher(template);
            StringBuffer result = new StringBuffer();
            while(matcher.find()) {
                String m = matcher.group();
                System.out.println(m);
                m = matcher.group(1);
                System.out.println(m);
                String scriptValue = StringUtils.toString(Ognl.getValue(m, root), true);
                matcher.appendReplacement(result, String.valueOf(scriptValue));
            }
            matcher.appendTail(result);
            System.out.println("result:"+result);

            String[] array = Pattern.compile(",").split(result);
            for (String str : array) {
                System.out.println(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJAXB() {

        try {
            FileInputStream fis = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\转换器加密.xml"));
            DataMap dataMap = JAXBUtils.deserialize(new InputStreamReader(fis), DataMap.class);
            System.out.println(dataMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testJson() {
        try {
            String mapXml = FileUtils.readFileToString(new File("C:\\Users\\Administrator\\Desktop\\映射\\testMapping.xml"), "utf-8");
            DataMap dataMap = RequestMapBuilder.build(mapXml);
            JsonMapper jsonMapper = new JsonMapper();
            MapperContext mapperContext = new MapperContext();
            mapperContext.setDataMap(dataMap);
            mapperContext.setPayload(JSON.parseObject(getRawJson()));
            System.out.println(jsonMapper.map(mapperContext));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJsonArray() {

        try {

            List<Map<String, Object>> teachers = new ArrayList<>();
            for(int index = 0; index < 3; index++) {
                Map<String, Object> teacher = new HashMap<>();
                teacher.put("id", index+10);
                teacher.put("name", "zhansan"+index);
                teachers.add(teacher);
            }

            String mapXml = FileUtils.readFileToString(new File("C:\\Users\\Administrator\\Desktop\\映射\\testNest.xml"), "utf-8");
            DataMap dataMap = RequestMapBuilder.build(mapXml);
            JsonMapper jsonMapper = new JsonMapper();
            MapperContext mapperContext = new MapperContext();
            mapperContext.setDataMap(dataMap);
            mapperContext.setPayload(JSON.parseArray(JSON.toJSONString(teachers)));
            System.out.println(jsonMapper.map(mapperContext));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testNestJson() {

        try {
            Map<String, Object> teacher = new HashMap<>();
            teacher.put("id", 101);
            teacher.put("name", "lisi");

            List<Map<String, Object>> parents = new ArrayList<>();
            for(int index = 0; index < 3; index++) {
                Map<String, Object> parent = new HashMap<>();
                parent.put("id", index+10);
                parent.put("name", "zhansan"+index);
                parents.add(parent);
            }

            teacher.put("parent", parents.get(0));
            teacher.put("parents", parents);

            String mapXml = FileUtils.readFileToString(new File("C:\\Users\\Administrator\\Desktop\\映射\\testNest.xml"), "utf-8");
            DataMap dataMap = RequestMapBuilder.build(mapXml);
            JsonMapper jsonMapper = new JsonMapper();
            MapperContext mapperContext = new MapperContext();
            mapperContext.setDataMap(dataMap);
            mapperContext.setPayload(JSON.parseObject(JSON.toJSONString(teacher)));
            System.out.println(jsonMapper.map(mapperContext));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testXml() {

        try {
            String mapXml = FileUtils.readFileToString(new File("C:\\Users\\Administrator\\Desktop\\映射\\testMapping.xml"), "utf-8");
            DataMap dataMap = RequestMapBuilder.build(mapXml);
            XmlMapper xmlMapper = new XmlMapper();
            MapperContext mapperContext = new MapperContext();
            mapperContext.setDataMap(dataMap);
            mapperContext.setPayload(JSON.parseObject(getRawJson()));
            System.out.println(xmlMapper.map(mapperContext));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testNestXml() {

        try {
            Map<String, Object> teacher = new HashMap<>();
            teacher.put("id", 101);
            teacher.put("name", "lisi");

            List<Map<String, Object>> parents = new ArrayList<>();
            for(int index = 0; index < 3; index++) {
                Map<String, Object> parent = new HashMap<>();
                parent.put("id", index+10);
                parent.put("name", "zhansan"+index);
                parents.add(parent);
            }

            teacher.put("parent", parents.get(0));
            teacher.put("parents", parents);

            String mapXml = FileUtils.readFileToString(new File("C:\\Users\\Administrator\\Desktop\\映射\\testNest.xml"), "utf-8");
            DataMap dataMap = RequestMapBuilder.build(mapXml);
            XmlMapper xmlMapper = new XmlMapper();
            MapperContext mapperContext = new MapperContext();
            mapperContext.setDataMap(dataMap);
            mapperContext.setPayload(JSON.parseObject(JSON.toJSONString(teacher)));
            System.out.println(xmlMapper.map(mapperContext));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Test
    public void testForm() {
        try {
            String mapXml = FileUtils.readFileToString(new File("C:\\Users\\Administrator\\Desktop\\映射\\testMapping.xml"), "utf-8");
            DataMap dataMap = RequestMapBuilder.build(mapXml);
            FormMapper formMapper = new FormMapper();
            MapperContext mapperContext = new MapperContext();
            mapperContext.setDataMap(dataMap);
            mapperContext.setPayload(JSON.parseObject(getRawJson()));
            System.out.println(formMapper.map(mapperContext));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testXmlBug() {
        try {
            String mapXml = FileUtils.readFileToString(new File("C:\\Users\\Administrator\\Desktop\\推送\\映射\\testXmlBug.xml"), "utf-8");
            DataMap dataMap = RequestMapBuilder.build(mapXml);
            XmlMapper xmlMapper = new XmlMapper();
            String xml = "{\"header\":{\"signed\":\"110ABE56EA0FB4226144E9E525AB38C3\"},\"body\":{\"order\":{\"orderId\":\"62986544\",\"policies\":[{\"credentials\":\"411522198803123782\",\"name\":\"测试\",\"policyStatus\":\"REQUEST_CANCLE\",\"remarks\":\"tzh测试\"},{\"credentials\":\"311522198803123782\",\"name\":\"测2\",\"policyStatus\":\"REQUEST_CANCLE\",\"remarks\":\"tzh测试\"}],\"paymentStatus\":\"PAYED\",\"status\":\"CANCEL\"}}}";
            MapperContext mapperContext = new MapperContext();
            mapperContext.setDataMap(dataMap);
            mapperContext.setPayload(JSON.parseObject(xml));
            System.out.println(xmlMapper.map(mapperContext));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getRawJson() {
        Map<String, Object> teacher = new HashMap<>();
        teacher.put("id", 101);
        teacher.put("name", "lisi");

        List<Map<String, Object>> students = new ArrayList<>();
        for(int index = 0; index < 3; index++) {
            Map<String, Object> student = new HashMap<>();
            student.put("id", index+10);
            student.put("name", "zhansan"+index);
            students.add(student);
        }

        Map<String, Object> student = new HashMap<>();
        student.put("id", 1);
        student.put("name", "zhansan1");

        teacher.put("student", student);
        teacher.put("students", students);

        String result = JSON.toJSONString(teacher);
        System.out.println("The raw json is "+result);
        return result;
    }


    @Test
    public void testEncrypt() {

        String encrypt = "#encryptKey+','+request.header.signed+','+request.body.order.orderId+','+request.body.order.policies[0].name+','+" +
                "concat(request.body.order.policies, item, item.name, ',')";

        String json = "{\"header\":{\"signed\":\"110ABE56EA0FB4226144E9E525AB38C3\"},\"body\":{\"order\":{\"orderId\":\"62986544\",\"policies\":[{\"credentials\":\"411522198803123782\",\"name\":\"测试\",\"policyStatus\":\"REQUEST_CANCLE\",\"remarks\":\"tzh测试\"},{\"credentials\":\"311522198803123782\",\"name\":\"测2\",\"policyStatus\":\"REQUEST_CANCLE\",\"remarks\":\"tzh测试\"}],\"paymentStatus\":\"PAYED\",\"status\":\"CANCEL\"}}}";

        XmlMapper xmlMapper = new XmlMapper();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("request", JSON.parseObject(json));

        try {
            String result = xmlMapper.evalScriptEL(jsonObject, encrypt, false, EncryptMethod.MD5, "lisi");
            System.out.println(result);
        } catch (OgnlException e) {
            e.printStackTrace();
        }

    }



}

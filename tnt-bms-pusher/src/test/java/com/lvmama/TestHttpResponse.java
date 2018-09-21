package com.lvmama;

import com.lvmama.bms.core.commons.utils.JAXBUtils;
import com.lvmama.bms.core.domain.enums.ConvertVersion;
import com.lvmama.bms.pusher.domain.MsgPusherAppContext;
import com.lvmama.bms.pusher.map.v1.RequestMapBuilder;
import com.lvmama.bms.pusher.map.v1.vo.DataMap;
import com.lvmama.bms.pusher.map.vo.ResponseMap;
import com.lvmama.bms.pusher.protocol.domain.ConvertMapper;
import com.lvmama.bms.pusher.protocol.http.HttpPusher;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.StringReader;

public class TestHttpResponse {

    @Test
    public void testXmlResponse() {

        MsgPusherAppContext appContext = new MsgPusherAppContext();
        HttpPusher httpPusher = new HttpPusher(appContext);

        try {
            String mapXml = FileUtils.readFileToString(new File("C:\\Users\\Administrator\\Desktop\\推送\\映射\\结果判断.xml"), "utf-8");
            ResponseMap responseMap = JAXBUtils.deserialize(new StringReader(mapXml), ResponseMap.class);
            ConvertMapper convertMapper = new ConvertMapper("", responseMap, ConvertVersion.V2, 10,"");

            boolean isSuccess = httpPusher.isSuccess(convertMapper, 200, "<response status=\"200\"><status>200</status></response>");
            System.out.println(isSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testJsonResponse() {
        MsgPusherAppContext appContext = new MsgPusherAppContext();
        HttpPusher httpPusher = new HttpPusher(appContext);

        try {
            String mapXml = FileUtils.readFileToString(new File("C:\\Users\\Administrator\\Desktop\\推送\\映射\\结果判断.xml"), "utf-8");
            ResponseMap responseMap = JAXBUtils.deserialize(new StringReader(mapXml), ResponseMap.class);
            ConvertMapper convertMapper = new ConvertMapper("", responseMap, ConvertVersion.V2, 10, "");
            boolean isSuccess = httpPusher.isSuccess(convertMapper, 200, "{\"response\": {\"status\":\"200\"}}");
            System.out.println(isSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

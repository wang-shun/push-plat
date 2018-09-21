package com.lvmama.meituan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.domain.Action;
import com.lvmama.bms.core.domain.enums.ConvertVersion;
import com.lvmama.bms.core.domain.enums.EncryptMethod;
import com.lvmama.bms.pusher.convert.MessageConvertFactory;
import com.lvmama.bms.pusher.convert.MessageConverter;
import com.lvmama.bms.pusher.map.MapperContext;
import com.lvmama.bms.pusher.protocol.http.HttpClientFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 *
 */
public class HttpClientTest {

    @Test
    public void testHeader() throws Exception {
        String url = "http://localhost:8580/pushJson";
        String payload = "{\"header\":{\"Authorization\":\"MWS lvyou_517:sePXuayH0jsRK6Q6yd819/A7Wdw=\",\"PartnerId\":\"196\",\"Date\":\"Thu, 12 Jul 2018 07:24:45 GMT\"},\"body\":{\"issueType\":1,\"describe\":\"出票成功\",\"partnerId\":196,\"body\":{\"orderId\":39,\"partnerOrderId\":\"66924195\",\"voucherType\":1,\"voucherPics\":[\"http://pic.lvmama.com/uploads/pc/place2/201807/2f6f87a4c078fee19032a68baad14cf4.jpg\"],\"vouchers\":[\"619995\"]}}}";
        MapperContext mapperContext = new MapperContext();
        mapperContext.setEncryptMethod(EncryptMethod.MD5);
        mapperContext.setEncryptKey("123");
        mapperContext.setPayload(JSONObject.parse(payload));
        mapperContext.setRequestMap("<script>payload.body</script>");
        mapperContext.setConvertVersion(ConvertVersion.V2);
        mapperContext.setHttpHeader("PartnerId=<script>payload.header.PartnerId</script>&Authorization=<script>payload.header.Authorization</script>&Date=<script>payload.header.Date</script>&");

        MessageConverter messageConverter = MessageConvertFactory.getMessageConverter("Json");

        String body = messageConverter.formatMessage(mapperContext);
        Map<String, String> headers = messageConverter.formatHeader(mapperContext);

        HttpPost httpPost = new HttpPost(url);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        StringEntity entity = new StringEntity(body, ContentType.create("application/json", Charset.forName("UTF-8")));

        httpPost.setEntity(entity);
        org.apache.http.Header[] header = httpPost.getAllHeaders();
        System.out.println(JSON.toJSONString(header));

        CloseableHttpResponse response = null;

        try {
            response = HttpClientFactory.getHttpClient().execute(httpPost);
            System.out.println(response);
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        } finally {
            if(response != null) {
                response.close();
            }
        }
    }
}

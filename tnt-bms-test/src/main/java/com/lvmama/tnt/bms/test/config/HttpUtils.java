package com.lvmama.tnt.bms.test.config;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.regex.Pattern;

public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils() {

    }

    private static final int TIME_OUT = 3000;

    public static String httpPost(String url, String paramJson) {
        return httpPost(url, paramJson, TIME_OUT);
    }

    public static String httpPost(String url, String paramJson, int timeOut) {
        CloseableHttpClient httpCilent = HttpClients.custom().build();
        // 配置超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeOut)
                .setConnectionRequestTimeout(timeOut).setSocketTimeout(timeOut).setRedirectsEnabled(true).build();
        HttpPost httpPost = new HttpPost(url);
        // 设置超时时间
        httpPost.setConfig(requestConfig);
        try {
            StringEntity entity = new StringEntity(paramJson, "UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            // 设置post求情参数
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpCilent.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (!Pattern.matches("^2\\d\\d$", String.valueOf(statusCode))) {
                LOGGER.error("请求失败，statusCode={}",statusCode);
                return "failed request";
            }
            return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
        }
        return null;
    }
}

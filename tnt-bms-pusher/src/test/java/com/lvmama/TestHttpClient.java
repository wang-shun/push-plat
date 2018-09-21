package com.lvmama;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestHttpClient {

    @Test
    public void testPool() {

        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        // Increase max total connection to 200
        cm.setMaxTotal(200);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(20);
        // Increase max connections for localhost:80 to 50
        HttpHost localhost = new HttpHost("locahost", 80);
        cm.setMaxPerRoute(new HttpRoute(localhost), 50);

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                // Close expired connections
                cm.closeExpiredConnections();
                // Optionally, close connections
                // that have been idle longer than 30 sec
                cm.closeIdleConnections(10000, TimeUnit.SECONDS);
            }
        }, 0, 1, TimeUnit.SECONDS);

//        DefaultConnectionKeepAliveStrategy
        ConnectionKeepAliveStrategy connectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() {

            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                // Honor 'keep-alive' header
                HeaderElementIterator it = new BasicHeaderElementIterator(
                        response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch(NumberFormatException ignore) {
                        }
                    }
                }
                HttpHost target = (HttpHost) context.getAttribute(
                        HttpClientContext.HTTP_TARGET_HOST);
                if ("www.naughty-server.com".equalsIgnoreCase(target.getHostName())) {
                    // Keep alive for 5 seconds only
                    return 5 * 1000;
                } else {
                    // otherwise keep alive for 30 seconds
                    return -1;
                }
            }

        };

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setConnectionReuseStrategy(new DefaultClientConnectionReuseStrategy())
                .setKeepAliveStrategy(connectionKeepAliveStrategy)
                .setRetryHandler(new DefaultHttpRequestRetryHandler())
                .build();


//        try {
//            HttpGet httpGet = new HttpGet("http://www.baidu.com");
//            CloseableHttpResponse response = httpClient.execute(httpGet);
//            System.out.println("responseContent: "+EntityUtils.toString(response.getEntity()));
//            System.out.println("responseStatus: "+response.getStatusLine().getStatusCode());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("name", "zhangsan"));
        params.add(new BasicNameValuePair("age", "20"));

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
//                .setConnectionRequestTimeout(10000)
                .build();

        for(int i = 0; i < 5; i++) {
            try {
                HttpPost httpPost = new HttpPost("http://127.0.0.1:8580/testHttpClient");
                httpPost.setConfig(requestConfig);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                CloseableHttpResponse response = httpClient.execute(httpPost);
//                httpPost.releaseConnection();
                System.out.println("responseContent: "+EntityUtils.toString(response.getEntity()));
                System.out.println("responseStatus: "+response.getStatusLine().getStatusCode());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Test
    public void testPostNameValuePair() {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("name", "zhangsan"));
            params.add(new BasicNameValuePair("age", "20"));

            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();

            HttpPost httpPost = new HttpPost("http://127.0.0.1:8580/testHttpClient");
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(new UrlEncodedFormEntity(params));


            CloseableHttpResponse response = httpClient.execute(httpPost);
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.out.println(response.getStatusLine().getStatusCode());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    @Test
    public void testPostMultiPart() {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpEntity multipartEntity = MultipartEntityBuilder.create()
                    .addPart("name", new StringBody("lisi"))
                    .addPart("age", new StringBody("20"))
                    .build();

            HttpPost httpPost = new HttpPost("testHttpClient");
            httpPost.setEntity(multipartEntity);

            CloseableHttpResponse response = httpClient.execute(new HttpHost("127.0.0.1", 8580, "http"), httpPost);
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.out.println(response.getStatusLine().getStatusCode());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    @Test
    public void testPostStringEntity() {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            StringEntity stringEntity = new StringEntity("name=lisi&age=20");
            stringEntity.setContentType("application/x-www-form-urlencoded");

            HttpPost httpPost = new HttpPost("http://127.0.0.1:8580/testHttpClient");
            httpPost.setEntity(stringEntity);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.out.println(response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }






}

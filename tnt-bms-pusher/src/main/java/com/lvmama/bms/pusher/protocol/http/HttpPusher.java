package com.lvmama.bms.pusher.protocol.http;

import com.alibaba.fastjson.JSON;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.domain.Action;
import com.lvmama.bms.core.domain.dto.MessageDTO;
import com.lvmama.bms.core.domain.enums.ConvertType;
import com.lvmama.bms.core.domain.enums.ConvertVersion;
import com.lvmama.bms.core.domain.enums.EncryptMethod;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.pusher.convert.MessageConvertFactory;
import com.lvmama.bms.pusher.convert.MessageConverter;
import com.lvmama.bms.pusher.domain.MsgPusherAppContext;
import com.lvmama.bms.pusher.map.MapperContext;
import com.lvmama.bms.pusher.map.vo.ResponseMap;
import com.lvmama.bms.pusher.protocol.ProtocolPusher;
import com.lvmama.bms.pusher.protocol.domain.ConvertMapper;
import com.lvmama.bms.pusher.support.MsgCacheManager;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpPusher implements ProtocolPusher {

    private Logger logger = LoggerFactory.getLogger(HttpPusher.class);

    private MsgCacheManager msgCacheManager;

    public HttpPusher(MsgPusherAppContext appContext) {
        msgCacheManager = appContext.getMsgCacheManager();
    }

    @Override
    public Action push(MessageDTO message) {

        ConvertType convertType = ConvertType.getTypeByValue(message.getConverterType());
        if(convertType == null) {
            logger.error("tip=Message ConvertType is null|"+message);
            return Action.EXECUTE_FAILED;
        }

        HttpContext httpContext = new HttpContext(message);

        try {

            MapperContext mapperContext = new MapperContext();
            mapperContext.setPayload(message.getMsgContent());
            mapperContext.setEncryptMethod(EncryptMethod.getEnumByValue(message.getEncryptMethod()));
            mapperContext.setEncryptKey(message.getEncryptKey());
            String paramName = null;

            ConvertMapper convertMapper = null;
            if(message.getConvertMapId() != null) {
                convertMapper = msgCacheManager.getConvertMapper(message.getConvertMapId());
                if(convertMapper != null) {
                    String requestMap = convertMapper.getRequestMap();
                    if (convertType != null && convertType.value() == 3 && StringUtils.isNotEmpty(requestMap)) {
                        String reg = "^\\$\\{[a-zA-Z]*\\}";
                        Matcher matcher = Pattern.compile(reg).matcher(requestMap);
                        if (matcher.find()) {
                            String patternStr = matcher.group();
                            paramName = patternStr.substring(2, patternStr.length() - 1);
                            requestMap = requestMap.substring(matcher.end() + 1);
                        }
                    }
                    mapperContext.setDataMap(convertMapper.getDataMap());
                    mapperContext.setRequestMap(requestMap);
                    mapperContext.setConvertVersion(convertMapper.getConvertVersion());
                    mapperContext.setHttpHeader(convertMapper.getHttpHeader());
                }
            }

            if(mapperContext.getConvertVersion() == null) {
                mapperContext.setConvertVersion(ConvertVersion.NotDefine);
            }

            MessageConverter messageConverter = MessageConvertFactory.getMessageConverter(convertType.name());
            long convertTimeCost = System.currentTimeMillis();

            String body = messageConverter.formatMessage(mapperContext);
            Map<String, String> headers = messageConverter.formatHeader(mapperContext);
            httpContext.setConvertTimeCost(System.currentTimeMillis() - convertTimeCost);
            httpContext.setRequestBody(body);

            HttpPost httpPost = new HttpPost(message.getPushUrl());

            if (headers != null && !headers.isEmpty()) {
                httpContext.setRequestHeader(JSON.toJSONString(headers));
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            if (message.getConnectTimeOut() != null && message.getReadTimeOut() != null) {
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(message.getReadTimeOut()).setConnectTimeout(message.getConnectTimeOut()).build();
                httpPost.setConfig(requestConfig);
            }

            StringEntity entity = null;
            if (StringUtils.isEmpty(paramName)) {
                entity = new StringEntity(body, ContentType.create(convertType.mimeType(), Charset.forName("UTF-8")));
            } else {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                pairs.add(new BasicNameValuePair(paramName, body));
                entity = new UrlEncodedFormEntity(pairs, "UTF-8");
            }
            httpPost.setEntity(entity);
            org.apache.http.Header[] header = httpPost.getAllHeaders();
            logger.info("headers:{}",header);

            long pushTimeCost = System.currentTimeMillis();
            CloseableHttpResponse response = null;

            try {
                response = HttpClientFactory.getHttpClient().execute(httpPost);
                httpContext.setPushTimeCost(System.currentTimeMillis() - pushTimeCost);
                httpContext.setResponseStatus(response.getStatusLine().getStatusCode());
                httpContext.setResponseContent(EntityUtils.toString(response.getEntity()));
            } catch (ClientProtocolException e) {
                logger.error("tip=fail push message|"+httpContext+"|exception=", e);
                return Action.EXECUTE_FAILED;
            } catch (IOException e) {
                if(httpContext.getPushTimeCost() == null) {
                    httpContext.setPushTimeCost(System.currentTimeMillis() - pushTimeCost);
                }
                logger.error("tip=fail push message|"+httpContext+"|exception=", e);
                return Action.EXECUTE_EXCEPTION;
            } finally {
                if(response != null) {
                    response.close();
                }
            }

            if (isSuccess(convertMapper, httpContext.getResponseStatus(), httpContext.getResponseContent())) {
                logger.info("tip=success push message|"+httpContext);
                return Action.EXECUTE_SUCCESS;
            } else {
                logger.info("tip=fail push message|"+httpContext);
                return Action.EXECUTE_EXCEPTION;
            }

        } catch (Exception e) {
            logger.error("tip=fail push message|"+httpContext+"|exception=", e);
            return Action.EXECUTE_EXCEPTION;
        }

    }

    public boolean isSuccess(ConvertMapper convertMapper, int statusCode, String responseContent) throws DocumentException, OgnlException {

        ResponseMap responseMap = convertMapper == null ? null : convertMapper.getResponseMap();
        if(responseMap == null) {
            return Pattern.matches("^2\\d\\d$", String.valueOf(statusCode));
        }

        switch (responseMap.getDecodeType()) {
            case Status : {
                String value = StringUtils.isEmpty(responseMap.getValue())? "^2\\d\\d$" : responseMap.getValue();
                if(Pattern.matches(value, String.valueOf(statusCode))) {
                    return true;
                }
            } break;
            case XML : {
                String path = responseMap.getPath();
                path = "/" + path.replace(".", "/");
                Document document = DocumentHelper.parseText(responseContent);
                Node node = document.selectSingleNode(path);
                if(responseMap.getValue().equals(node.getText())) {
                    return true;
                }
            } break;
            case JSON : {
                String value = Ognl.getValue(responseMap.getPath(), JSON.parse(responseContent)).toString();
                if(responseMap.getValue().equals(value)) {
                    return true;
                }
            }
        }

        return false;

    }

    private class HttpContext {

        public HttpContext(MessageDTO message) {
            this.message = message;
        }

        private MessageDTO message;

        private String requestHeader;

        private String requestBody;

        private Integer responseStatus;

        private String responseContent;

        private Long convertTimeCost;

        private Long pushTimeCost;

        public String getRequestHeader() {
            return requestHeader;
        }

        public void setRequestHeader(String requestHeader) {
            this.requestHeader = requestHeader;
        }

        public String getRequestBody() {
            return requestBody;
        }

        public void setRequestBody(String requestBody) {
            this.requestBody = requestBody;
        }

        public Integer getResponseStatus() {
            return responseStatus;
        }

        public void setResponseStatus(Integer responseStatus) {
            this.responseStatus = responseStatus;
        }

        public String getResponseContent() {
            return responseContent;
        }

        public void setResponseContent(String responseContent) {
            this.responseContent = responseContent;
        }

        public Long getConvertTimeCost() {
            return convertTimeCost;
        }

        public void setConvertTimeCost(Long convertTimeCost) {
            this.convertTimeCost = convertTimeCost;
        }

        public Long getPushTimeCost() {
            return pushTimeCost;
        }

        public void setPushTimeCost(Long pushTimeCost) {
            this.pushTimeCost = pushTimeCost;
        }

        public MessageDTO getMessage() {
            return message;
        }

        public void setMessage(MessageDTO message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return
                    "|responseStatus=" + responseStatus +
                    "|convertTimeCost=" + convertTimeCost + "ms" +
                    "|pushTimeCost=" + pushTimeCost + "ms" +
                    "|message=" + message +
                    "|responseContent=" + responseContent +
                    "|requestHeader=" + requestHeader +
                    "|requestBody=" + requestBody ;
        }
    }

}

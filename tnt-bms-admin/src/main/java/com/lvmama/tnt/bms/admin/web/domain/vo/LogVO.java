package com.lvmama.tnt.bms.admin.web.domain.vo;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 */
public class LogVO implements Serializable{
    public static class Request {
        private Integer pageNo;
        private Integer pageSize;
        private String msgId;
        private String token;
        private String msgType;
        private String pushStatus;
        private String queryTime[];

        public Integer getPageNo() {
            return pageNo;
        }

        public void setPageNo(Integer pageNo) {
            this.pageNo = pageNo;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getPushStatus() {
            return pushStatus;
        }

        public void setPushStatus(String pushStatus) {
            this.pushStatus = pushStatus;
        }

        public String[] getQueryTime() {
            return queryTime;
        }

        public void setQueryTime(String[] queryTime) {
            this.queryTime = queryTime;
        }

        @Override
        public String toString() {
            return "request{" +
                    "msgId='" + msgId + '\'' +
                    ", token='" + token + '\'' +
                    ", msgType='" + msgType + '\'' +
                    ", pushStatus='" + pushStatus + '\'' +
                    ", queryTime=" + Arrays.toString(queryTime) +
                    '}';
        }
    }

    public static class Response {
        private String id;
        public String msgId;
        private String msgType;
        private String token;
        private String distributeName;
        private String responseStatus;
        private String maxRetry;
        private String receiveTime;
        private String pushTimeCost;
        private String retryCount;
        private String convertMapId;
        private String requestBody;
        private String responseContent;
        private String requestHeader;
        private String exception;
        private String msgContent;
        private String pushStatus;

        public String getPushStatus() {
            return pushStatus;
        }

        public void setPushStatus(String pushStatus) {
            this.pushStatus = pushStatus;
        }

        public String getMsgContent() {
            return msgContent;
        }

        public void setMsgContent(String msgContent) {
            this.msgContent = msgContent;
        }

        public String getException() {
            return exception;
        }

        public void setException(String exception) {
            this.exception = exception;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getDistributeName() {
            return distributeName;
        }

        public void setDistributeName(String distributeName) {
            this.distributeName = distributeName;
        }

        public String getResponseStatus() {
            return responseStatus;
        }

        public void setResponseStatus(String responseStatus) {
            this.responseStatus = responseStatus;
        }

        public String getMaxRetry() {
            return maxRetry;
        }

        public void setMaxRetry(String maxRetry) {
            this.maxRetry = maxRetry;
        }

        public String getReceiveTime() {
            return receiveTime;
        }

        public void setReceiveTime(String receiveTime) {
            this.receiveTime = receiveTime;
        }

        public String getPushTimeCost() {
            return pushTimeCost;
        }

        public void setPushTimeCost(String pushTimeCost) {
            this.pushTimeCost = pushTimeCost;
        }

        public String getRetryCount() {
            return retryCount;
        }

        public void setRetryCount(String retryCount) {
            this.retryCount = retryCount;
        }

        public String getConvertMapId() {
            return convertMapId;
        }

        public void setConvertMapId(String convertMapId) {
            this.convertMapId = convertMapId;
        }

        public String getRequestBody() {
            return requestBody;
        }

        public void setRequestBody(String requestBody) {
            this.requestBody = requestBody;
        }

        public String getResponseContent() {
            return responseContent;
        }

        public void setResponseContent(String responseContent) {
            this.responseContent = responseContent;
        }

        public String getRequestHeader() {
            return requestHeader;
        }

        public void setRequestHeader(String requestHeader) {
            this.requestHeader = requestHeader;
        }

        @Override
        public String toString() {
            return "Resposne{" +
                    "msgId='" + msgId + '\'' +
                    ", msgType='" + msgType + '\'' +
                    ", token='" + token + '\'' +
                    ", distributeName='" + distributeName + '\'' +
                    ", responseStatus='" + responseStatus + '\'' +
                    ", maxRetry='" + maxRetry + '\'' +
                    ", receiveTime='" + receiveTime + '\'' +
                    ", pushTimeCost='" + pushTimeCost + '\'' +
                    ", retryCount='" + retryCount + '\'' +
                    ", convertMapId='" + convertMapId + '\'' +
                    ", requestBody='" + requestBody + '\'' +
                    ", responseContent='" + responseContent + '\'' +
                    ", requestHeader='" + requestHeader + '\'' +
                    '}';
        }
    }
}

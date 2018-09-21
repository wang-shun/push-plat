package com.lvmama.tnt.bms.admin.web.configure;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.util.StringUtils;

import javax.jms.Queue;


/**
 *
 */
@Configuration
public class ActivemqConfig {
    private static final String TNT_BMS_SCHEDULER_SYNC = "TNT_BMS_SCHEDULER_SYNC";
    private static final String TNT_BMS_PUSHER_SYNC = "TNT_BMS_PUSHER_SYNC";
    private static final String TNT_BMS_MANUAL_SEND = "TNT_BMS_MANUAL_SEND";

    private String userName;

    private String password;

    @Value("${jms_server}")
    private String brokerURL;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)) {
            return new ActiveMQConnectionFactory(userName, password, brokerURL);
        } else {
            return new ActiveMQConnectionFactory(brokerURL);
        }
    }

    /**
     * 映射器配置队列
     * @return
     */
    @Bean("queueConvert")
    public ActiveMQTopic queueConvert(){
        return new ActiveMQTopic(TNT_BMS_PUSHER_SYNC);
    }
    /**
     * 配置同步队列
     * @return
     */
    @Bean("queueAccess")
    public ActiveMQTopic queueAccess(){
        return new ActiveMQTopic(TNT_BMS_SCHEDULER_SYNC);
    }
    /**
     * 手动发送队列
     * @return
     */
    @Bean("queueManual")
    public Queue queueManual(){
        return new ActiveMQQueue(TNT_BMS_MANUAL_SEND);
    }


    @Bean
    public JmsMessagingTemplate jmsMessagingTemplate(ActiveMQConnectionFactory connectionFactory){
        JmsMessagingTemplate jmsMessagingTemplate =  new JmsMessagingTemplate(connectionFactory);
//        jmsMessagingTemplate.setJmsMessageConverter(new SyncEventMessageConverter());
        return jmsMessagingTemplate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBrokerURL() {
        return brokerURL;
    }

    public void setBrokerURL(String brokerURL) {
        this.brokerURL = brokerURL;
    }
}

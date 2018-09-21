package com.lvmama.bms.scheduler.support;

import com.alibaba.fastjson.JSON;
import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.domain.Message;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.bms.core.protocol.command.MsgSendRequest;
import com.lvmama.bms.scheduler.domain.MsgSchedulerAppContext;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Collections;


public class MsgManualSend {

    private Logger logger = LoggerFactory.getLogger(MsgManualSend.class);

    private MessageReceiver messageReceiver;

    private ConnectionFactory connectionFactory;

    private String QueueName = "TNT_BMS_MANUAL_SEND";

    public MsgManualSend(MsgSchedulerAppContext appContext) {
        messageReceiver = appContext.getMessageReceiver();
        String brokerURL = appContext.getConfig().getParameter(ExtConfig.MQ_BROKER_URL);//appContext.getConfig().getParameter("", "tcp://10.200.6.197:61616");
        connectionFactory = new ActiveMQConnectionFactory(brokerURL);
    }

    public void start() {

        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(QueueName);
            MessageConsumer consumer = session.createConsumer(queue);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(javax.jms.Message message) {
                    try {
                        logger.info("tip=receive manual push message|message={}", message);
                        MsgSendRequest request = new MsgSendRequest();
                        SyncEvent syncEvent = JSON.parseObject(((TextMessage) message).getText(), SyncEvent.class);
                        Message manualMessage = JSON.parseObject(syncEvent.getAddition(), Message.class);
                        manualMessage.checkField();
                        manualMessage.setPayload(manualMessage.getPayload());
                        request.setMessages(Collections.singletonList(manualMessage));
                        messageReceiver.receive(request);
                    }  catch (Exception e) {
                        logger.error("failed manual send message, the request message="+message, e);
                    }
                }
            });
        } catch (JMSException e) {
            logger.error("The MsgManualSend failed start", e);
        }

    }

}

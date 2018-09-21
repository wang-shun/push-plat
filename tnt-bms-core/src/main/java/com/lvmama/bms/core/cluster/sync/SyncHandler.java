package com.lvmama.bms.core.cluster.sync;

import com.alibaba.fastjson.JSON;
import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.commons.concurrent.ConcurrentHashSet;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import org.apache.activemq.pool.PooledConnectionFactory;

import javax.jms.*;

public class SyncHandler {

    private Logger logger = LoggerFactory.getLogger(SyncHandler.class);

    private ConnectionFactory connectionFactory;

    private String TopicName;

    private ConcurrentHashSet<SyncListener> listeners = new ConcurrentHashSet<SyncListener>();

    public SyncHandler(Config config) {
        this.TopicName = config.getParameter(ExtConfig.MQ_SYNC_TOPIC);
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(config.getParameter(ExtConfig.MQ_BROKER_URL));
        pooledConnectionFactory.setMaxConnections(30);
        this.connectionFactory = pooledConnectionFactory;
    }

    public void start() throws Exception {

        final Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TopicName);
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    logger.info("tip=receive sync message|message={}", message);
                    TextMessage textMessage = (TextMessage) message;
                    SyncEvent syncEvent = JSON.parseObject(textMessage.getText(), SyncEvent.class);
                    for(SyncListener listener : listeners) {
                        listener.onSyncEvent(syncEvent);
                    }
                } catch (Exception e) {
                    logger.error("tip=fail sync message config|message="+message+"|exception=", e);
                }
            }
        });

        connection.start();

    }

    public void sync(SyncEvent syncEvent) {

        Connection connection = null;
        Session session = null;
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();
            Topic topic = session.createTopic(TopicName);
            MessageProducer producer = session.createProducer(topic);
            TextMessage message = session.createTextMessage();
            message.setText(JSON.toJSONString(syncEvent));
            producer.send(message);
        } catch (Exception e) {
            logger.error("tip=fail sync message config|"+syncEvent+"|exception=", e);
        } finally {
            if(session!=null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if(connection!=null) {
                try {
                    connection.stop();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addListener(SyncListener syncListener) {
        this.listeners.add(syncListener);
    }

}

package com.lvmama.tnt.bms.admin.web.service.impl;

import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;
import com.lvmama.tnt.bms.admin.web.domain.po.BaseDO;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsAccessDO;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import com.lvmama.tnt.bms.admin.web.service.SendConfigService;
import com.lvmama.tnt.bms.admin.web.util.FastJsonUtil;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author longhr
 * @version 2017/11/21 0021
 */
@Service
public class SendConfigServiceImpl implements SendConfigService {
    private Logger logger = LoggerFactory.getLogger(SendConfigServiceImpl.class);

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    @Qualifier("queueAccess")
    private ActiveMQTopic queueAccess;


    @Override
    public void sendMessage(SyncEvent syncEvent) {
        jmsMessagingTemplate.convertAndSend(queueAccess,FastJsonUtil.toJsonString(syncEvent));
    }

    @Override
    public <T> void sendMessage(SyncEvent.ObjectType type, SyncEvent.EventType operate, T messageContext) throws BusinessException {
        if (StringUtils.isEmpty(type)) {
            throw new BusinessException("消息类型不能为空");
        }
        if (StringUtils.isEmpty(operate)) {
            throw new BusinessException("消息操作类型不能为空");
        }
        if (messageContext == null) {
            throw new BusinessException("消息内容不能为空");
        }
        simplify(messageContext);
        String content = FastJsonUtil.toJsonString(messageContext);
        logger.info("发送消息={}",content);
        SyncEvent message = new SyncEvent();
        message.setObjectType(type);
        message.setEventType(operate);
        message.setAddition(content);
        jmsMessagingTemplate.convertAndSend(queueAccess,FastJsonUtil.toJsonString(message));
    }

    @Override
    public <T> void sendNewsTypeMessage(SyncEvent.EventType operate, T messageContext) throws BusinessException {
        this.sendMessage(SyncEvent.ObjectType.MSG_TYPE,operate,messageContext);
    }

    @Override
    public <T> void sendAccessMessage(SyncEvent.EventType operate, T messageContext) throws BusinessException {
        this.sendMessage(SyncEvent.ObjectType.ACCESS,operate,messageContext);
    }

    @Override
    public <T> void sendBatchAccessMessage(SyncEvent.EventType operate, List<T> messageContexts) throws BusinessException {
        for (T message : messageContexts) {
            this.sendAccessMessage(operate, message);
        }
    }

    private <T> void simplify(T messageContext) {
        if (messageContext instanceof NewsTypeDO) {
            NewsTypeDO newsTypeDO = (NewsTypeDO) messageContext;
            newsTypeDO.setIsOpen(newsTypeDO.getOpened());
            newsTypeDO.setOpened(null);
        } else if (messageContext instanceof NewsAccessDO) {
            NewsAccessDO newsAccessDO = (NewsAccessDO) messageContext;
            newsAccessDO.setIsOpen(newsAccessDO.getOpened());
            newsAccessDO.setOpened(null);
            newsAccessDO.setRpcPusherId(newsAccessDO.getPushID());
            newsAccessDO.setPushID(null);
        }
    }
}

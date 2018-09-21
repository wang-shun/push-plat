package com.lvmama.tnt.bms.admin.web.service.impl;

import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;
import com.lvmama.tnt.bms.admin.web.domain.po.ConvertDO;
import com.lvmama.tnt.bms.admin.web.mapper.ConvertMapper;
import com.lvmama.tnt.bms.admin.web.service.ConvertManageService;
import com.lvmama.tnt.bms.admin.web.util.FastJsonUtil;
import com.lvmama.tnt.bms.admin.web.util.PageUtil;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 */
@Service
public class ConvertManageServiceImpl implements ConvertManageService {

    @Autowired
    private ConvertMapper convertMapper;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    @Qualifier("queueConvert")
    private ActiveMQTopic queueConvert;

    @Override
    public ConvertDO findByID(Integer id) {
        return convertMapper.findByID(id);
    }

    @Override
    public List<ConvertDO> findByName(String name) {
        return convertMapper.findByName(name);
    }

    @Override
    public List<ConvertDO> findAll() {
        return convertMapper.findAll();
    }

    @Override
    public List<ConvertDO> findByPage(ConvertDO convertDO, int pageNo, int pageSize){
        return convertMapper.findByPage(convertDO, PageUtil.getRowBounds(pageNo, pageSize));
    }

    @Override
    public long totalCount(ConvertDO convertDO) {
        return convertMapper.totalCount(convertDO);
    }

    @Override
    public boolean checkExist(String name) {
        return convertMapper.existName(name) > 0;
    }

    @Override
    @Transactional
    public int insert(ConvertDO convertDO) {
        convertDO.setVersion(1);
        int insertCount = convertMapper.insert(convertDO);
        sendMessage(SyncEvent.EventType.Add,convertDO);
        return insertCount;
    }

    @Override
    @Transactional
    public int update(ConvertDO convertDO) {
        if (convertDO.getVersion() == null) {
            convertDO.setVersion(convertMapper.dataVersion(convertDO.getName())+1);
        } else {
            convertDO.setVersion(convertDO.getVersion()+1);
        }

        sendMessage(SyncEvent.EventType.Update,convertDO);
        return convertMapper.update(convertDO);
    }

    @Override
    @Transactional
    public int delete(Integer id) {
        ConvertDO convertDO = convertMapper.findByID(id);
        sendMessage(SyncEvent.EventType.Delete,convertDO);
        return convertMapper.delete(id);
    }

    @Override
    public int deleteByName(String name) {
        List<ConvertDO> convertDOS = convertMapper.findByName(name);
        for (ConvertDO convertDO : convertDOS) {
            if (name.equals(convertDO.getName())) {
                sendMessage(SyncEvent.EventType.Delete, convertDO);
            }
        }
        return convertMapper.deleteByName(name);
    }

    public void sendMessage(SyncEvent.EventType operate, ConvertDO messageContext) {
        SyncEvent message = new SyncEvent();
        message.setObjectType(SyncEvent.ObjectType.MSG_CONVERTER);
        message.setEventType(operate);
        message.setAddition(FastJsonUtil.toJsonString(messageContext));
        jmsMessagingTemplate.convertAndSend(queueConvert,FastJsonUtil.toJsonString(message));
    }
}

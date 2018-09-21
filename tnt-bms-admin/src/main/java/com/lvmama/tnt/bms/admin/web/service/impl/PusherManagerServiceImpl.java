package com.lvmama.tnt.bms.admin.web.service.impl;

import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;
import com.lvmama.tnt.bms.admin.web.domain.po.MsgPusherDO;
import com.lvmama.tnt.bms.admin.web.mapper.PusherManagerMapper;
import com.lvmama.tnt.bms.admin.web.service.PusherManagerService;
import com.lvmama.tnt.bms.admin.web.util.FastJsonUtil;
import com.lvmama.tnt.bms.admin.web.util.PageUtil;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 *
 */
@Service
public class PusherManagerServiceImpl implements PusherManagerService {

    @Autowired
    private PusherManagerMapper pusherManagerMapper;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    @Qualifier("queueConvert")
    private ActiveMQTopic queueConvert;

    @Override
    public MsgPusherDO findByID(Integer id, boolean include) {
        String includeStr = include ? "Y":null;
        return pusherManagerMapper.findById(id,includeStr);
    }

    @Override
    public List<MsgPusherDO> findByName(String name, boolean include) {
        String includeStr = include ? "Y":null;
        return pusherManagerMapper.findByName(name,includeStr);
    }

    @Override
    public long totalCount(MsgPusherDO msgPusherDO) {
        return pusherManagerMapper.totalCount(msgPusherDO);
    }

    @Override
    public List<MsgPusherDO> findPushersByPage(MsgPusherDO msgPusherDO, int pageNo, int pageSize) {
        return pusherManagerMapper.findPushersByPage(msgPusherDO, PageUtil.getRowBounds(pageNo,pageSize));
    }

    @Override
    public List<MsgPusherDO> findPushers() {
        return pusherManagerMapper.findPushers();
    }

    @Override
    public boolean checkExist(String name) {
        return pusherManagerMapper.existName(name) > 0;
    }

    @Override
    @Transactional
    public int insert(MsgPusherDO msgPusherDO) {
        msgPusherDO.setVersion(1);
        int insertCount = pusherManagerMapper.insert(msgPusherDO);
        sendMessage(SyncEvent.EventType.Add, msgPusherDO);
        return insertCount;
    }

    @Override
    @Transactional
    public int update(MsgPusherDO msgPusherDO) {
        if (msgPusherDO.getVersion() == null) {
            msgPusherDO.setVersion(pusherManagerMapper.dataVersion(msgPusherDO.getName())+1);
        } else {
            msgPusherDO.setVersion(msgPusherDO.getVersion()+1);
        }
        int update =  pusherManagerMapper.update(msgPusherDO);
        sendMessage(SyncEvent.EventType.Update, msgPusherDO);
        return update;
    }

    @Override
    @Transactional
    public int delete(Integer id) {
        MsgPusherDO msgPusherDO = pusherManagerMapper.findById(id, null);
        sendMessage(SyncEvent.EventType.Delete, msgPusherDO);
        return pusherManagerMapper.delete(id);
    }

    @Override
    public int deleteByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return 0;
        }
        List<MsgPusherDO> msgPusherDOs = pusherManagerMapper.findByName(name, null);
        for (MsgPusherDO pusherDO : msgPusherDOs) {
            if (name.equals(pusherDO.getName())) {
                sendMessage(SyncEvent.EventType.Delete, pusherDO);
            }
        }
        return pusherManagerMapper.deleteByName(name);
    }

    private void sendMessage(SyncEvent.EventType operate, MsgPusherDO messageContext) {
        SyncEvent message = new SyncEvent();
        message.setObjectType(SyncEvent.ObjectType.MSG_PUSHER);
        message.setEventType(operate);
        messageContext.setJar(null);
        message.setAddition(FastJsonUtil.toJsonString(messageContext));
        jmsMessagingTemplate.convertAndSend(queueConvert,FastJsonUtil.toJsonString(message));
    }
}

package com.lvmama.tnt.bms.admin.web.service.impl;

import com.alibaba.dubbo.common.json.JSONArray;
import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.bms.core.json.JSON;
import com.lvmama.tnt.bms.admin.web.domain.vo.MessageVO;
import com.lvmama.tnt.bms.admin.web.service.ManualPushService;
import com.lvmama.tnt.bms.admin.web.util.FastJsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Queue;

/**
 *
 */
@Service
public class ManualPushServiceImpl implements ManualPushService {

    private static final String SPLIT_CHARACTER = ",";

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    @Qualifier("queueManual")
    private Queue queueManual;

    @Override
    public void pushNews(MessageVO messageVO) {
        com.lvmama.bms.core.domain.Message pushMsg = new com.lvmama.bms.core.domain.Message();
        pushMsg.setMsgId(messageVO.getMsgId().trim());
        pushMsg.setMsgType(messageVO.getMsgType().trim());
        pushMsg.setMaxRetry(StringUtils.isEmpty(messageVO.getMaxRetry())?0:Integer.parseInt(messageVO.getMaxRetry()));
        pushMsg.setReplaceOnExist(Boolean.parseBoolean(messageVO.getReplaceOnExist()));

        String tokens = messageVO.getTokens().trim();
        if (StringUtils.isNotEmpty(tokens)) {
            pushMsg.setTokens(tokens.split(SPLIT_CHARACTER));
        }
        pushMsg.setPayload(messageVO.getPayload());


        SyncEvent message = new SyncEvent();
        message.setObjectType(SyncEvent.ObjectType.MANUAL_PUSH);
        message.setEventType(SyncEvent.EventType.Add);
        message.setAddition(FastJsonUtil.toJsonString(pushMsg));
        jmsMessagingTemplate.convertAndSend(queueManual,FastJsonUtil.toJsonString(message));
    }


}

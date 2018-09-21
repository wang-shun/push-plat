package com.lvmama.tnt.bms.admin.web.remote;

import com.alibaba.dubbo.config.annotation.Service;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import com.lvmama.tnt.bms.admin.web.mapper.MessageMapper;
import com.lvmama.tnt.bms.admin.web.service.NewsTypeService;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;
import com.lvmama.tnt.bms.api.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@Service
public class MessageServiceImpl extends AbstractRemoteService implements IMessageService {

    @Autowired
    private NewsTypeService newsTypeService;

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public ResponseDTO<Boolean> messageExist(String msgType, String msgId) {
        if (StringUtils.isEmpty(msgId) || StringUtils.isEmpty(msgType)) {
            return returnFailed("param is empty");
        }
        NewsTypeDO typeDO = newsTypeService.findByType(msgType);
        if (typeDO == null) {
            return returnFailed("没有这种消息类型");
        }
        //message表中是否存在
        Long msgTypeId = typeDO.getId();
        Boolean result = messageMapper.checkMessageByMsgId(msgTypeId, msgId) > 0;
        //message 中不存在 去失败表中查
        if (!result) {
            result = messageMapper.checkFailureByMsgId(msgId) > 0;
        }
        ResponseDTO responseDTO = returnSuccess();
        responseDTO.setResult(result);
        return responseDTO;
    }
}
package com.lvmama.tnt.bms.api.service;

import com.lvmama.tnt.bms.api.domain.ResponseDTO;

/**
 *
 */
public interface IMessageService {

    ResponseDTO<Boolean> messageExist(String msgType, String msgId);
}

package com.lvmama.bms.client.support;

import com.lvmama.bms.core.domain.Message;
import com.lvmama.bms.core.exception.MsgSendException;

import java.util.List;

/**
 * @author Robert HG (254963746@qq.com) on 5/21/15.
 */
public interface MsgSendExecutor<T> {

    T execute(List<Message> messages) throws MsgSendException;

}

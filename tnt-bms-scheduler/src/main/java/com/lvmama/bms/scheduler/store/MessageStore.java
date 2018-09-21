package com.lvmama.bms.scheduler.store;

import com.lvmama.bms.scheduler.store.domain.po.MessagePO;

import java.util.Map;
import java.util.Set;

public interface MessageStore {

     String replaceOnExist(MessagePO messagePO);

     boolean saveMessageAndToken(MessagePO messagePO, Integer[] tokenIds);

     boolean saveMessage(MessagePO messagePO);

     String updateMessage(MessagePO messagePO);

     boolean saveMsgWithCheck(MessagePO messagePO);

     Map<String, MessagePO> getMessage(Set<Long> ids, Integer msgTypeId);

     Long updateStatus(String msgId, Integer initialStatus, Integer targetStatus, Integer msgTypeId);

     int batchUpdateStatus(Set<Long> msgIds, Integer initialStatus, Integer targetStatus, Integer msgTypeId);

     int deleteExpiredMsg(Integer msgTypeId);


}

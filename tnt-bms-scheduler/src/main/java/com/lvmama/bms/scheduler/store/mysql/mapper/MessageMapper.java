package com.lvmama.bms.scheduler.store.mysql.mapper;


import com.lvmama.bms.scheduler.store.domain.po.MessagePO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Set;

public interface MessageMapper {

     int saveMessage(MessagePO message);

     String updateMessage(MessagePO messagePo);

     int saveMsgWithCheck(MessagePO message);

     @MapKey("id")
     Map<String, MessagePO> getMessage(@Param("msgIds") Set<Long> msgIds, @Param("msgTypeId") Integer msgTypeId);

     int batchUpdateStatus(@Param("msgIds") Set<Long> msgIds,
                         @Param("initialStatus") Integer initialStatus,
                         @Param("targetStatus") Integer targetStatus,
                         @Param("msgTypeId") Integer msgTypeId);

     Long updateStatus(@Param("msgId") String msgId,
                      @Param("initialStatus") Integer initialStatus,
                      @Param("targetStatus") Integer targetStatus,
                      @Param("msgTypeId") Integer msgTypeId);

     int deleteExpiredMsg(@Param("msgTypeId") Integer msgTypeId);

}

package com.lvmama.tnt.bms.admin.web.mapper;


import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
public interface MessageMapper {

    /**
     * message表中查询是否存在
     * @param typeId 消息类型主键ID
     * @param msgId 业务ID
     * @return
     */
    int checkMessageByMsgId(@Param("typeId") Long typeId, @Param("msgId") String msgId);

    /**
     * 失败表中查询是否存在
     * @param msgId
     * @return
     */
    int checkFailureByMsgId(String msgId);

    /**
     * 删除token时，msg_token清除
     * @param typeId
     * @param tokenIds
     * @return
     */
    int deleteMessageToken(@Param("typeId") Long typeId, @Param("tokenIds") List<Long> tokenIds);
}

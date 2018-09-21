package com.lvmama.bms.scheduler.store.mysql.mapper;

import com.lvmama.bms.scheduler.store.domain.po.MsgTokenPO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface MsgTokenMapper {

    int saveMsgToken(@Param("msgId") Long msgId,
                     @Param("tokenIds") Integer[] tokenIds,
                     @Param("msgTypeId") Integer msgTypeId);

    int deleteMsgToken(@Param("msgTokenId") Long msgTokenId,
                       @Param("msgTypeId") Integer msgTypeId);

    List<MsgTokenPO> queryMsgToken(@Param("tokenInclude") Set<Integer> tokenInclude,
                                   @Param("tokenExclude") Set<Integer> tokenExclude,
                                   @Param("count") Integer count,
                                   @Param("msgTypeId") Integer msgTypeId,
                                   @Param("status") Integer status);

    boolean updateStatus(@Param("msgTokenId") Long msgTokenId,
                         @Param("status") Integer status,
                         @Param("msgTypeId") Integer msgTypeId);

    boolean batchUpdateStatus(@Param("msgTokens") List<MsgTokenPO> msgTokens,
                              @Param("status") Integer status,
                              @Param("msgTypeId") Integer msgTypeId);

    List<MsgTokenPO> queryMsgTokenBy(@Param("status") Integer status,
                                     @Param("startTime") String startTime,
                                     @Param("endTime") String endTime,
                                     @Param("msgTypeId") Integer msgTypeId);

    int deleteByMsgId(@Param("msgId") Long msgId,
                      @Param("msgTypeId") Integer msgTypeId);

    int recoverDeathMsgToken(@Param("initialStatus") Integer initialStatus,
                @Param("targetStatus") Integer targetStatus,
                @Param("deathTime") Date deathTime,
                @Param("msgTypeId") Integer msgTypeId);


    int updateRetryStatus(@Param("msgTokenId") Long msgTokenId,
                          @Param("retryTime") Date retryTime,
                          @Param("retryCount") Integer retryCount,
                          @Param("status") Integer status,
                          @Param("msgTypeId") Integer msgTypeId);


    int deleteExpiredMsgToken(@Param("tokenId") Integer tokenId,
                              @Param("msgTypeId") Integer msgTypeId);


}

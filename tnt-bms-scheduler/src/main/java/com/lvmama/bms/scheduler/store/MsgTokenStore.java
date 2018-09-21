package com.lvmama.bms.scheduler.store;

import com.lvmama.bms.scheduler.store.domain.po.MsgTokenPO;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface MsgTokenStore {

    boolean saveMsgToken(Long msgId, Integer[] tokenIds, Integer msgTypeId);

    boolean deleteMsgToken(Long msgTokenId, Integer msgTypeId);

    List<MsgTokenPO> getMsgToken(Set<Integer> tokenInclude, Set<Integer> tokenExclude,
                                 Integer dataCount, Integer msgTypeId, Integer status);

    boolean updateStatus(Long msgTokenId, Integer status, Integer msgTypeId);

    int updateRetryStatus(Long msgTokenId, Date retryTime, Integer retryCount, Integer status, Integer msgTypeId);

    boolean batchUpdateStatus(List<MsgTokenPO> msgTokens, Integer status, Integer msgTypeId);

    List<MsgTokenPO> getMsgToken(Integer status, String startTime, String endTime, Integer msgTypeId);

    int deleteByMsgId(Long msgId, Integer msgTypeId);

    int recoverDeathMsgToken(Integer initialStatus, Integer targetStatus, Date deathTime, Integer msgTypeId);

    int deleteExpiredMsgToken(Integer tokenId, Integer msgTypeId);

}

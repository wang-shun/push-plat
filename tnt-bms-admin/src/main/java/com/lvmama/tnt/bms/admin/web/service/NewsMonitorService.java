package com.lvmama.tnt.bms.admin.web.service;

import com.lvmama.tnt.bms.admin.web.domain.po.ReceiverMonitorDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.RequestVO;

import java.util.List;

/**
 * @author longhr
 * @version 2017/12/5 0005
 */
public interface NewsMonitorService {

    List<ReceiverMonitorDO> findByInterval(RequestVO requestVO);

    ReceiverMonitorDO findByID(Long id);

    int insert(ReceiverMonitorDO receiverMonitorDO);

    int insertBatch(List<ReceiverMonitorDO> list);

    int update(ReceiverMonitorDO receiverMonitorDO);

    List<ReceiverMonitorDO> countSendGroupByToken(Long startTime, Long endTime);
}

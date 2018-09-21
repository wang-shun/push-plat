package com.lvmama.tnt.bms.admin.web.mapper;

import com.lvmama.tnt.bms.admin.web.domain.po.ReceiverMonitorDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author longhr
 * @version 2017/12/5 0005
 */
@Repository
public interface ReceiverMonitorMapper {

    List<ReceiverMonitorDO> findByInterval(@Param("token") String token, @Param("startTime") Long startTime, @Param("endTime") Long endTime);

    ReceiverMonitorDO findByID(Long id);

    int insert(ReceiverMonitorDO receiverMonitorDO);

    int insertBatch(List<ReceiverMonitorDO> list);

    int update(ReceiverMonitorDO receiverMonitorDO);

    List<ReceiverMonitorDO> countSendGroupByToken(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}

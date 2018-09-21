package com.lvmama.bms.scheduler.store.mysql.mapper;

import com.lvmama.bms.scheduler.store.domain.po.statis.MsgTypeStatisPO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface MsgTypeStatisMapper {

    int batchSave(@Param("msgTypeStatis") Collection<MsgTypeStatisPO> msgTypeStatis);

    List<MsgTypeStatisPO> sum(
            @Param("after") Long after,
            @Param("before") Long before);


}

package com.lvmama.tnt.bms.admin.web.mapper;

import com.lvmama.tnt.bms.admin.web.domain.po.PushFailureDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface PushFailureMapper {

    int delete(Integer id);

    PushFailureDO findById(Integer id);

    List<PushFailureDO> findByPage(PushFailureDO pushFailureDO);

    public long totalCount(PushFailureDO pushFailureDO);

    int insert(PushFailureDO pushFailureDO);

    int batchInsert(List<PushFailureDO> pushFailureDOS);
}

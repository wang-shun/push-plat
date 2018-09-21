package com.lvmama.tnt.bms.admin.web.mapper;

import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeStatisticsDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author longhr
 * @version 2017/11/8 0008
 */
@Repository
public interface NewsTypeStatisticsMapper {

    int insert(NewsTypeStatisticsDO newsTypeStatisticsDO);

    int update(NewsTypeStatisticsDO newsTypeStatisticsDO);

    int delete(Long id);

    List<NewsTypeStatisticsDO> findNewsTypeStatis(@Param("msgTypeID") Long msgTypeID, @Param("startTime") Long startTime, @Param("endTime") Long endTime);
}

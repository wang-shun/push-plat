package com.lvmama.tnt.bms.test.mapper;

import com.lvmama.tnt.bms.test.domain.po.CountPO;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface SendCountMapper {

    @Insert("insert into t_bms_count(sendCount,createTime) values(#{sendCount},#{createTime})")
    void save(CountPO countPO);

}

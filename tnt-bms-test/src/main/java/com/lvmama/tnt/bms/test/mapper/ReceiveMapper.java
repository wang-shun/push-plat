package com.lvmama.tnt.bms.test.mapper;

import com.lvmama.tnt.bms.test.domain.po.ReceivePO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface ReceiveMapper {

    @Insert("insert into t_bms_receive(context) values(#{context})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void save(ReceivePO receivePO);
}

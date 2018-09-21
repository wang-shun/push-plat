package com.lvmama.tnt.bms.test.mapper;

/**
 *
 */

import com.lvmama.tnt.bms.test.domain.po.ConvertPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConvertMapper {

    @Insert("insert into t_convert(type,convertMap) values(#{type},#{convertMap})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void save(ConvertPO convertPO);

    @Select("select * from t_convert")
    List<ConvertPO> findAll();
}

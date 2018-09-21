package com.lvmama.tnt.bms.test.mapper;

import com.lvmama.tnt.bms.test.domain.po.DistributorPO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface DistributorMapper {

    @Insert("insert into t_bms_distributor(`name`,`token`,convertMapID,convertType) values(#{name},#{token},#{convertMapID},#{convertType})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void save(DistributorPO distributorPO);

    @Update("update t_bms_distributor set `token`=#{token} where id = #{id}")
    void update(DistributorPO distributorPO);

    @Select("select id,`name`,`token`,convertMap from t_bms_distributor")
    List<DistributorPO> findAll();

    @Select("select count(*) from t_bms_distributor")
    int distributorCount();

    @Select("select * from t_bms_distributor limit #{offset},#{pageSize}")
    List<DistributorPO> findByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);
}

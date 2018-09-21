package com.lvmama.tnt.bms.test.mapper;

import com.lvmama.tnt.bms.test.domain.po.UrlMapPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface UrlMapMapper {

    @Insert("insert into t_urlMap(url) values(#{url})")
    void save(UrlMapPO urlMap);

    @Select("select * from t_urlMap")
    List<UrlMapPO> findAll();
}

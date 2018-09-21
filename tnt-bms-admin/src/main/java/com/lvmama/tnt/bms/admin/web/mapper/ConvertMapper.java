package com.lvmama.tnt.bms.admin.web.mapper;

import com.lvmama.tnt.bms.admin.web.domain.po.ConvertDO;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface ConvertMapper {

    ConvertDO findByID(Integer id);

    List<ConvertDO> findByName(String name);

    List<ConvertDO> findAll();

    List<ConvertDO> findByPage(ConvertDO convertDO, RowBounds rowBounds);

    long totalCount(ConvertDO convertDO);

    int existName(String name);

    int insert(ConvertDO convertDO);

    int update(ConvertDO convertDO);

    int delete(Integer id);

    int deleteByName(String name);

    int dataVersion(String name);
}

package com.lvmama.tnt.bms.admin.web.mapper;

import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author longhr
 * @version 2017/11/1 0001
 */
@Repository
public interface NewsTypeMapper {

    List<NewsTypeDO> findByPage(NewsTypeDO newsTypeDO, RowBounds rowBounds);

    long totalCount(NewsTypeDO newsTypeDO);

    List<NewsTypeDO> findTypeListByParam(Map<String, String> paramMap);

    List<String> findTypeList(Map<String, String> paramMap);

    NewsTypeDO findById(Long id);

    NewsTypeDO findByType(String type);

    int insert(NewsTypeDO newsTypeDO);

    int insertBatch(List<NewsTypeDO> newsTypeDOS);

    int update(NewsTypeDO newsTypeDO);

    int delete(Long id);

    int deleteByName(String name);

    int dataVersion(String name);

    String existTable(@Param("tableName") String tableName);

    void dropTable(@Param("tableName") String tableName);

    void createMsgTable(@Param("tableName") String tableName);

    void createMsgTokenTable(@Param("tableName") String tableName);
}

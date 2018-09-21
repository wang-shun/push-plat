package com.lvmama.tnt.bms.admin.web.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Repository
public interface QueryDataMapper {
    List<Map<String, Object>> querySqlListByAnyMysqlSql(Map<String, String> map);
}

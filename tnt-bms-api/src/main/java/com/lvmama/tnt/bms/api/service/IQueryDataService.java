package com.lvmama.tnt.bms.api.service;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface IQueryDataService {
    List<Map<String,Object>> querySqlListByAnyMysqlSql(Map<String, String> map);
}

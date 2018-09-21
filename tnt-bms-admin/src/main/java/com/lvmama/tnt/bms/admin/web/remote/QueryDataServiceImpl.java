package com.lvmama.tnt.bms.admin.web.remote;

import com.alibaba.dubbo.config.annotation.Service;
import com.lvmama.tnt.bms.admin.web.mapper.QueryDataMapper;
import com.lvmama.tnt.bms.api.service.IQueryDataService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class QueryDataServiceImpl implements IQueryDataService {

    @Autowired
    private QueryDataMapper queryDataMapper;

    @Override
    public List<Map<String, Object>> querySqlListByAnyMysqlSql(Map<String, String> map) {
        return queryDataMapper.querySqlListByAnyMysqlSql(map);
    }
}

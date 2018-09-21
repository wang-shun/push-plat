package com.lvmama.tnt.bms.admin.web.service;

import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.PageResultVO;

import java.util.List;
import java.util.Map;

/**
 * @author longhr
 * @version 2017/11/1 0001
 */
public interface NewsTypeService {

    List<NewsTypeDO> findTypeListByParam(Map<String, String> paramMap);

    PageResultVO<List<NewsTypeDO>> findByPage(NewsTypeDO newsTypeDO, int pageNo, int pageSize);

    List<NewsTypeDO> findTypeListByParam(String typeName);

    List<String> findTypeList();

    NewsTypeDO findById(Long id);

    boolean existName(String name);

    int insert(NewsTypeDO newsTypeDO);

    int update(NewsTypeDO newsTypeDO);

    int delete(Long id);

    //remote
    int deleteByName(String typeName);

    NewsTypeDO findByType(String msgType);
}

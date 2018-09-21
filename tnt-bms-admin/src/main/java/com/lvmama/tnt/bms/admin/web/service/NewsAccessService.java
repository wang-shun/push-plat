package com.lvmama.tnt.bms.admin.web.service;

import com.lvmama.tnt.bms.api.domain.NewsAccessDTO;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsAccessDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author longhr
 * @version 2017/11/2 0002
 */
public interface NewsAccessService {

    void reloadCache();

    String updateForAPI(NewsAccessDTO newsAccessDTO);

    String insertForAPI(NewsAccessDTO newsAccessDTO);

    NewsAccessDO findFullAccessInfo(Long id);

    List<NewsAccessDO> findGroupsForChoose();

    List<NewsAccessDO> findByGroupID(Long groupID);

    NewsAccessDO findById(Long id);

    NewsAccessDO findByToken(String token);

    NewsAccessDO findByName(String name);

    NewsAccessDO findByParam(NewsAccessDO newsAccessDO);

    List<NewsAccessDO> findByPage(NewsAccessDO newsAccessDO, int pageNo, int pageSize);

    long totalCount(NewsAccessDO accessDO);

    NewsAccessDO findAccessGroupByToken(String token);

    int deleteByToken(String token);

    int insert(NewsAccessDO newsAccessDO);

    int update(NewsAccessDO newsAccessDO);

    int delete(Long id);

    int removeGroup(Long id);

    //remote
    List<NewsAccessDO> findByGroupToken(String groupToken);

    boolean checkExist(String groupToken, String name);

    List<NewsAccessDO> findByTokenBatch(List<String> tokens);

    int deleteMessageToken(Long typeId, List<Long> tokenIds);

    List<NewsAccessDO> fuzzyByName(String keyword);

    Map<String, NewsAccessDO> findNameByTokens(List<String> tokens);
}

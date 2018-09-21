package com.lvmama.tnt.bms.admin.web.mapper;

import com.lvmama.tnt.bms.admin.web.domain.po.NewsAccessDO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author longhr
 * @version 2017/11/2 0002
 */
@Repository
public interface NewsAccessMapper {

    NewsAccessDO findFullAccessInfo(Long id);

    List<NewsAccessDO> findGroupsForChoose();

    List<NewsAccessDO> findByGroupID(Long groupID);

    NewsAccessDO findById(Long id);

    public NewsAccessDO findByToken(String token);

    NewsAccessDO findByName(String name);

    NewsAccessDO findByParam(NewsAccessDO newsAccessDO);

    List<NewsAccessDO> findByPage(NewsAccessDO newsAccessDO, RowBounds rowBounds);

    long totalCount(NewsAccessDO newsAccessDO);

    NewsAccessDO findAccessGroupByToken(String token);

    int existName(String name);

    int insert(NewsAccessDO newsAccessDO);

    int update(NewsAccessDO newsAccessDO);

    int delete(Long id);

    int dataVersion(String token);

    int deleteByGroupId(Long groupId);

    int deleteByToken(String token);

    List<NewsAccessDO> findByGroupToken(String groupToken);

    int checkExist(@Param("groupToken") String groupToken, @Param("name") String name);

    List<NewsAccessDO> findByTokenBatch(List<String> tokens);

    List<NewsAccessDO> fuzzyByName(String keyword);

    @MapKey("token")
    Map<String, NewsAccessDO> findNameByTokens(List<String> tokens);

}

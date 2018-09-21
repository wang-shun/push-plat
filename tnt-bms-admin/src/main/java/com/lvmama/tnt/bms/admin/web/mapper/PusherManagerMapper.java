package com.lvmama.tnt.bms.admin.web.mapper;

import com.lvmama.tnt.bms.admin.web.domain.po.MsgPusherDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface PusherManagerMapper {
    MsgPusherDO findById(@Param("id") Integer id, @Param("include") String include);

    List<MsgPusherDO> findByName(@Param("name") String name, @Param("include") String  include);

    long totalCount(MsgPusherDO msgPusherDO);

    List<MsgPusherDO> findPushersByPage(MsgPusherDO msgPusherDO, RowBounds rowBounds);

    List<MsgPusherDO> findPushers();

    int existName(String name);

    int insert(MsgPusherDO msgPusherDO);

    int update(MsgPusherDO msgPusherDO);

    int delete(Integer id);

    int deleteByName(String name);

    int dataVersion(String name);
}

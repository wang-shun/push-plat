package com.lvmama.tnt.bms.admin.web.service;

import com.lvmama.tnt.bms.admin.web.domain.po.MsgPusherDO;

import java.util.List;

/**
 *
 */
public interface PusherManagerService {
    MsgPusherDO findByID(Integer id, boolean include);

    List<MsgPusherDO> findByName(String name, boolean include);

    long totalCount(MsgPusherDO msgPusherDO);

    List<MsgPusherDO> findPushersByPage(MsgPusherDO msgPusherDO, int pageNo, int pageSize);

    List<MsgPusherDO> findPushers();

    boolean checkExist(String name);



    int insert(MsgPusherDO msgPusherDO);

    int update(MsgPusherDO msgPusherDO);

    int delete(Integer id);

    int deleteByName(String name);
}

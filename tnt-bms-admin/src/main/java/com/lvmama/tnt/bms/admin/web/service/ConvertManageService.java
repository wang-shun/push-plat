package com.lvmama.tnt.bms.admin.web.service;

import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.tnt.bms.admin.web.domain.po.ConvertDO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 *
 */
public interface ConvertManageService {
    ConvertDO findByID(Integer id);

    List<ConvertDO> findByName(String name);

    List<ConvertDO> findAll();

    List<ConvertDO> findByPage(ConvertDO convertDO, int pageNo, int pageSize);

    long totalCount(ConvertDO convertDO);

    boolean checkExist(String name);

    int insert(ConvertDO convertDO);

    int update(ConvertDO convertDO);

    int delete(Integer id);

    int deleteByName(String name);

    void sendMessage(SyncEvent.EventType operate, ConvertDO messageContext);

}

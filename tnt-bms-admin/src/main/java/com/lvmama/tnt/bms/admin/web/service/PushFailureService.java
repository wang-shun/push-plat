package com.lvmama.tnt.bms.admin.web.service;

import com.lvmama.tnt.bms.admin.web.domain.po.PushFailureDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.PageResultVO;

import java.util.List;

/**
 *
 */
public interface PushFailureService {

    PageResultVO<List<PushFailureDO>> findByPage(PushFailureDO pushFailureDO, int pageNo, int pageSize);

    boolean delete(Integer id);

    boolean send(Integer id);
}

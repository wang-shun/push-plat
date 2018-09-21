package com.lvmama.tnt.bms.admin.web.search;


import com.lvmama.tnt.bms.admin.web.domain.vo.LogVO;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;

import java.util.List;

/**
 *
 */

public interface TntBmsLogService {

    ResponseDTO<List<LogVO.Response>> searchLog(LogVO.Request request);

    ResponseDTO<LogVO.Response> queryById(String id);
}

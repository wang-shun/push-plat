package com.lvmama.tnt.bms.admin.web.service;

import com.lvmama.tnt.bms.admin.web.domain.po.GlobalStatisticsDO;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeStatisticsDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.ChartDataVO;
import com.lvmama.tnt.bms.admin.web.domain.vo.PageResultVO;
import com.lvmama.tnt.bms.admin.web.domain.vo.RequestVO;
import com.lvmama.tnt.bms.admin.web.domain.vo.ResponseVO;

import java.util.List;

/**
 * @author longhr
 * @version 2017/11/8 0008
 */
public interface ChartService {

    List<GlobalStatisticsDO> getGlobalDataList(RequestVO requestVO);

    List<NewsTypeStatisticsDO> getNewsTypeDataList(RequestVO requestVO);

    ChartDataVO getGlobalData(RequestVO requestVO);

    ChartDataVO getNewsTypeData(RequestVO requestVO);

    PageResultVO findNewsSendSpeed(String msgId, String msgType, String startTime, String endTime, int pageNo, int pageSize);

    PageResultVO findNewsSpeedDetaiList(String msgId, String token, String msgType, Integer status, int pageNo, int pageSize);
}

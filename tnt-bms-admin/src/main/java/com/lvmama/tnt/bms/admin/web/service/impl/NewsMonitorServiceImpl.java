package com.lvmama.tnt.bms.admin.web.service.impl;

import com.lvmama.tnt.bms.admin.web.domain.po.ReceiverMonitorDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.RequestVO;
import com.lvmama.tnt.bms.admin.web.mapper.ReceiverMonitorMapper;
import com.lvmama.tnt.bms.admin.web.service.NewsMonitorService;
import com.lvmama.tnt.bms.admin.web.util.BmsDateUtils;
import com.lvmama.tnt.bms.admin.web.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author longhr
 * @version 2017/12/5 0005
 */
@Service
public class NewsMonitorServiceImpl implements NewsMonitorService {
    @Autowired
    private ReceiverMonitorMapper receiverMonitorMapper;

    @Override
    public List<ReceiverMonitorDO> findByInterval(RequestVO requestVO) {
        Long startTime = BmsDateUtils.getTimeMillis(requestVO.getStartTime());
        Long endTime = BmsDateUtils.getTimeMillis(requestVO.getEndTime());
        return receiverMonitorMapper.findByInterval(requestVO.getToken(), startTime, endTime);
    }

    @Override
    public ReceiverMonitorDO findByID(Long id) {
        return receiverMonitorMapper.findByID(id);
    }

    @Override
    public int insert(ReceiverMonitorDO receiverMonitorDO) {
        return receiverMonitorMapper.insert(receiverMonitorDO);
    }

    @Override
    public int insertBatch(List<ReceiverMonitorDO> list) {
        return receiverMonitorMapper.insertBatch(list);
    }

    @Override
    public int update(ReceiverMonitorDO receiverMonitorDO) {
        return receiverMonitorMapper.update(receiverMonitorDO);
    }

    public List<ReceiverMonitorDO> countSendGroupByToken(Long startTime, Long endTime){
        return receiverMonitorMapper.countSendGroupByToken(startTime, endTime);
    }
}

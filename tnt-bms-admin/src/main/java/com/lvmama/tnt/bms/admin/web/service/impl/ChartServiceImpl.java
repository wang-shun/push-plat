package com.lvmama.tnt.bms.admin.web.service.impl;

import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;
import com.lvmama.tnt.bms.admin.web.domain.po.*;
import com.lvmama.tnt.bms.admin.web.domain.vo.ChartDataVO;
import com.lvmama.tnt.bms.admin.web.domain.vo.PageResultVO;
import com.lvmama.tnt.bms.admin.web.domain.vo.RequestVO;
import com.lvmama.tnt.bms.admin.web.mapper.GlobalStatisticsMapper;
import com.lvmama.tnt.bms.admin.web.mapper.NewsSendSpeedMapper;
import com.lvmama.tnt.bms.admin.web.mapper.NewsTypeMapper;
import com.lvmama.tnt.bms.admin.web.mapper.NewsTypeStatisticsMapper;
import com.lvmama.tnt.bms.admin.web.service.ChartService;
import com.lvmama.tnt.bms.admin.web.util.BmsDateUtils;
import com.lvmama.tnt.bms.admin.web.domain.define.Constants;
import com.lvmama.tnt.bms.admin.web.util.FastJsonUtil;
import com.lvmama.tnt.bms.admin.web.util.PageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author longhr
 * @version 2017/11/8 0008
 */
@Service
public class ChartServiceImpl implements ChartService {
    private Logger logger = LoggerFactory.getLogger(ChartServiceImpl.class);

    @Autowired
    private GlobalStatisticsMapper globalStatisticsMapper;
    @Autowired
    private NewsTypeStatisticsMapper newsTypeStatisticsMapper;
    @Autowired
    private NewsSendSpeedMapper newsSendSpeedMapper;
    @Autowired
    private NewsTypeMapper newsTypeMapper;

    private int getStep(int size) {
        return size % Constants.SHOW_SIZE == 0 ? size / Constants.SHOW_SIZE : size / Constants.SHOW_SIZE + 1;
    }

    @Override
    public List<GlobalStatisticsDO> getGlobalDataList(RequestVO requestVO) {
        List<GlobalStatisticsDO> list = null;
        if (requestVO != null) {
            Long startTime = BmsDateUtils.getTimeMillis(requestVO.getStartTime());
            Long endTime = BmsDateUtils.getTimeMillis(requestVO.getEndTime());
            list = globalStatisticsMapper.findByTimeSlot(startTime, endTime);
        }
        return list;
    }

    @Override
    public List<NewsTypeStatisticsDO> getNewsTypeDataList(RequestVO requestVO) {
        List<NewsTypeStatisticsDO> list = null;
        if (requestVO != null) {
            Long startTime = BmsDateUtils.getTimeMillis(requestVO.getStartTime());
            Long endTime = BmsDateUtils.getTimeMillis(requestVO.getEndTime());
            Long typeID = null;
            if (!StringUtils.isEmpty(requestVO.getMsgType())) {
                typeID = newsTypeMapper.findByType(requestVO.getMsgType()).getId();
            }
            list = newsTypeStatisticsMapper.findNewsTypeStatis(typeID, startTime, endTime);
        }
        return list;
    }

    @Override
    public ChartDataVO getGlobalData(RequestVO requestVO) {
        ChartDataVO chartDataVO = new ChartDataVO();
        if (requestVO != null) {
            Long startTime = BmsDateUtils.getTimeMillis(requestVO.getStartTime());
            Long endTime = BmsDateUtils.getTimeMillis(requestVO.getEndTime());
            List<GlobalStatisticsDO> list = globalStatisticsMapper.findByTimeSlot(startTime, endTime);
            int size = list.size();
            int step = 1;
            if (size > Constants.SHOW_SIZE) {
                step = getStep(size);
            }
            GlobalStatisticsDO statisticsDO = null;
            List<String> lables = new ArrayList<>();
            List<String> reachCounts = new ArrayList<>();
            List<String> receiveCounts = new ArrayList<>();
            List<String> failCounts = new ArrayList<>();
            List<String> sendCounts = new ArrayList<>();
            for (int i = 0; i < size; i = i + step) {
                statisticsDO = list.get(i);
                lables.add(BmsDateUtils.getTimeFormat(statisticsDO.getTimestamp(),"MM-dd HH:mm"));
                reachCounts.add(String.valueOf(statisticsDO.getReachCount()));
                receiveCounts.add(String.valueOf(statisticsDO.getReceiveCount()));
                failCounts.add(String.valueOf(statisticsDO.getFailCount()));
                sendCounts.add(String.valueOf(statisticsDO.getSendCount()));
            }

            Map map = new HashMap();
            map.put("reachCounts", reachCounts);
            map.put("receiveCounts", receiveCounts);
            map.put("failCounts", failCounts);
            map.put("sendCounts", sendCounts);

            chartDataVO.setLabels(lables);
            chartDataVO.setDatasetsMap(map);

        }
        return chartDataVO;
    }

    @Override
    public ChartDataVO getNewsTypeData(RequestVO requestVO) {
        ChartDataVO chartDataVO = new ChartDataVO();
        if (requestVO != null) {
            Long startTime = BmsDateUtils.getTimeMillis(requestVO.getStartTime());
            Long endTime = BmsDateUtils.getTimeMillis(requestVO.getEndTime());
            Long typeID = null;
            if (!StringUtils.isEmpty(requestVO.getMsgType())) {
                typeID = newsTypeMapper.findByType(requestVO.getMsgType()).getId();
            }
            List<NewsTypeStatisticsDO> list = newsTypeStatisticsMapper.findNewsTypeStatis(typeID, startTime, endTime);
            int size = list.size();
            int step = 1;
            if (size > Constants.SHOW_SIZE) {
                step = getStep(size);
            }
            NewsTypeStatisticsDO statisticsDO = null;
            List<String> lables = new ArrayList<>();
            List<String> reachCounts = new ArrayList<>();
            List<String> receiveCounts = new ArrayList<>();
            List<String> failCounts = new ArrayList<>();
            List<String> sendCounts = new ArrayList<>();
            for (int i = 0; i < size; i = i + step) {
                statisticsDO = list.get(i);
                lables.add(BmsDateUtils.getTimeFormat(statisticsDO.getTimestamp(),"MM-dd HH:mm"));
                reachCounts.add(String.valueOf(statisticsDO.getReachCount()));
                receiveCounts.add(String.valueOf(statisticsDO.getReceiveCount()));
                failCounts.add(String.valueOf(statisticsDO.getFailCount()));
                sendCounts.add(String.valueOf(statisticsDO.getSendCount()));
            }

            Map map = new HashMap();
            map.put("reachCounts", reachCounts);
            map.put("receiveCounts", receiveCounts);
            map.put("failCounts", failCounts);
            map.put("sendCounts", sendCounts);

            chartDataVO.setLabels(lables);
            chartDataVO.setDatasetsMap(map);

        }
        return chartDataVO;
    }

    @Override
    public PageResultVO findNewsSendSpeed(String msgId, String msgType, String startTime, String endTime, int pageNo, int pageSize) {
        Long start = BmsDateUtils.getTimeMillis(startTime);
        Long end = BmsDateUtils.getTimeMillis(endTime);
        //MessageType
        NewsTypeDO newsTypeDO = newsTypeMapper.findByType(msgType);
        Long msgTypeID = null;
        List<String> messageIds = null;
        if (newsTypeDO != null) {
            msgTypeID = newsTypeDO.getId();
            //BusinessID to MessageID
            if (!StringUtils.isEmpty(msgId)) {
                messageIds = newsSendSpeedMapper.findMessageID(String.valueOf(msgTypeID),msgId);
                if (messageIds != null && messageIds.size() == 0) {
                    messageIds = null;
                }
            }
        }
        logger.info("messageIds:{}",messageIds);
        long totalCount = newsSendSpeedMapper.totalCount(messageIds, msgTypeID, start, end);
        long totalPage = PageUtil.calculateTotalPage(totalCount, pageSize);
        PageResultVO responseVO = new PageResultVO();
        responseVO.setPageNo(pageNo);
        responseVO.setTotalCount(totalCount);
        responseVO.setTotalPage(totalPage);
        responseVO.setPageSize(pageSize);
        List<NewsSendSpeedDO> list =  newsSendSpeedMapper.findByPage(messageIds, msgTypeID, start, end, PageUtil.getRowBounds(pageNo, pageSize));
        List<NewsSendSpeedDO> newList = new ArrayList<>(list.size());
        for (NewsSendSpeedDO sendSpeedDO : list) {
            if (StringUtils.isEmpty(sendSpeedDO.getMsgTypeId())) {
                continue;
            }
            MessageDO message = newsSendSpeedMapper.findMsgInfo(sendSpeedDO.getMsgTypeId(), sendSpeedDO.getMsgId());
            if (message != null) {
                sendSpeedDO.setBusinessMsgID(message.getMsgId());
                sendSpeedDO.setReceiveTime(message.getReceiveTime());
            }
            newList.add(sendSpeedDO);
        }
        responseVO.setResult(newList);
        return responseVO;
    }

    @Override
    public PageResultVO findNewsSpeedDetaiList(String msgId, String distributorName, String msgTypeId, Integer status, int pageNo, int pageSize) {
        logger.info("查询参数:{},{},{}",msgId,distributorName,msgTypeId);

        String tableName = "tnt_bms_msg_token_" + msgTypeId;
        logger.info("分表名：{}",tableName);

        long totalCount = newsSendSpeedMapper.totalSpeedDetailCount(msgId, distributorName, msgTypeId, status);
        long totalPage = PageUtil.calculateTotalPage(totalCount, pageSize);
        PageResultVO responseVO = new PageResultVO();
        responseVO.setPageNo(pageNo);
        responseVO.setTotalCount(totalCount);
        responseVO.setTotalPage(totalPage);
        responseVO.setPageSize(pageSize);
        responseVO.setResult(newsSendSpeedMapper.findSpeedDetailByPage(msgId, distributorName, msgTypeId, status, PageUtil.getRowBounds(pageNo, pageSize)));
        return responseVO;
    }
}

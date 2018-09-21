package com.lvmama.tnt.bms.admin.web.service.impl;

import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.tnt.bms.admin.web.domain.define.Constants;
import com.lvmama.tnt.bms.admin.web.domain.define.MessageConstants;
import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.PageResultVO;
import com.lvmama.tnt.bms.admin.web.mapper.NewsTypeMapper;
import com.lvmama.tnt.bms.admin.web.service.NewsTypeService;
import com.lvmama.tnt.bms.admin.web.service.SendConfigService;
import com.lvmama.tnt.bms.admin.web.util.FastJsonUtil;
import com.lvmama.tnt.bms.admin.web.util.PageUtil;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author longhr
 * @version 2017/11/1 0001
 */
@Service
public class NewsTypeServiceImpl implements NewsTypeService {

    private Logger logger = LoggerFactory.getLogger(NewsTypeServiceImpl.class);

    @Autowired
    private NewsTypeMapper newsTypeMapper;
    @Autowired
    private SendConfigService sendConfigService;

    @Override
    public List<NewsTypeDO> findTypeListByParam(Map<String, String> paramMap) {
        return newsTypeMapper.findTypeListByParam(paramMap);
    }

    @Override
    public PageResultVO<List<NewsTypeDO>> findByPage(NewsTypeDO newsTypeDO, int pageNo, int pageSize) {
        PageResultVO<List<NewsTypeDO>> resultVo = new PageResultVO<List<NewsTypeDO>>();
        long totalCount = newsTypeMapper.totalCount(newsTypeDO);
        long totalPage = PageUtil.calculateTotalPage(totalCount, pageSize);
        RowBounds rowBounds = new RowBounds((pageNo - 1) * pageSize, pageSize);
        resultVo.setResult(newsTypeMapper.findByPage(newsTypeDO, rowBounds));
        resultVo.setTotalCount(totalCount);
        resultVo.setTotalPage(totalPage);
        resultVo.setPageNo(pageNo);
        return resultVo;
    }

    @Override
    public List<NewsTypeDO> findTypeListByParam(String typeName) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("typeName", typeName);
        return newsTypeMapper.findTypeListByParam(paramMap);
    }

    @Override
    public List<String> findTypeList() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("typeName", "");
        return newsTypeMapper.findTypeList(paramMap);
    }

    @Override
    public NewsTypeDO findById(Long id) {
        return newsTypeMapper.findById(id);
    }

    @Override
    public boolean existName(String name) {
        long existCount = newsTypeMapper.totalCount(new NewsTypeDO(name));
        return existCount > 0;
    }

    @Override
    @Transactional
    public int insert(NewsTypeDO newsTypeDO) {
        int returnT = 0;
        long existCount = newsTypeMapper.totalCount(new NewsTypeDO(newsTypeDO.getType()));
        if (existCount > 0) {
            throw new BusinessException("该消息类型已存在！");
        }
        if (newsTypeDO.getOpened() == -1) {
            newsTypeDO.setOpened(Constants.OPENED);
        }
        if (newsTypeDO.getPriority() == -1) {
            newsTypeDO.setPriority(Constants.NORM_PRIORITY);
        }
        newsTypeDO.setModifyTime(new Date());
        newsTypeDO.setCreateTime(new Date());
        newsTypeDO.setVersion(1);
        returnT = newsTypeMapper.insert(newsTypeDO);

        Long typeID = newsTypeDO.getId();
        String msgTable = "tnt_bms_msg_" + typeID;
        String msgTokenTable = "tnt_bms_msg_token_" + typeID;
        logger.info("创建消息表：{},{}",msgTable,msgTokenTable);
        String exist = newsTypeMapper.existTable(msgTable);
        if (StringUtils.isEmpty(exist)) {
            newsTypeMapper.createMsgTable(msgTable);
        }
        exist = newsTypeMapper.existTable(msgTokenTable);
        if (StringUtils.isEmpty(exist)) {
            newsTypeMapper.createMsgTokenTable(msgTokenTable);
        }

        sendConfigService.sendNewsTypeMessage(SyncEvent.EventType.Add,newsTypeDO);

        return returnT;
    }

    @Override
    @Transactional
    public int update(NewsTypeDO newsTypeDO) {
        if (newsTypeDO != null) {
            newsTypeDO.setModifyTime(new Date());
            if (newsTypeDO.getOpened() == -1) {
                newsTypeDO.setOpened(1);
            }
            if (newsTypeDO.getVersion() != null) {
                newsTypeDO.setVersion(newsTypeDO.getVersion() + 1);
            } else {
                newsTypeDO.setVersion(newsTypeMapper.dataVersion(newsTypeDO.getType()) + 1);
            }
            int updateCount = newsTypeMapper.update(newsTypeDO);
            try {
                sendConfigService.sendNewsTypeMessage(SyncEvent.EventType.Update,newsTypeDO);
            } catch (BusinessException e) {
                throw e;
            }
            return updateCount;
        }
        return 0;
    }

    @Override
    public int delete(Long id) {
        try {
            NewsTypeDO newsTypeDO = newsTypeMapper.findById(id);
            sendConfigService.sendNewsTypeMessage(SyncEvent.EventType.Delete,newsTypeDO);
        } catch (BusinessException e) {
            throw e;
        }
        return newsTypeMapper.delete(id);
    }

    @Override
    public int deleteByName(String typeName) {
        try {
            NewsTypeDO newsTypeDO = newsTypeMapper.findByType(typeName);
            sendConfigService.sendNewsTypeMessage(SyncEvent.EventType.Delete,newsTypeDO);
        } catch (BusinessException e) {
            throw e;
        }
        return newsTypeMapper.deleteByName(typeName);
    }

    @Override
    public NewsTypeDO findByType(String msgType) {
        return newsTypeMapper.findByType(msgType);
    }
}

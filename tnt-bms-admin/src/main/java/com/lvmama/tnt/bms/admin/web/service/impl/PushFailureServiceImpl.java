package com.lvmama.tnt.bms.admin.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import com.lvmama.tnt.bms.admin.web.domain.po.PushFailureDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.MessageVO;
import com.lvmama.tnt.bms.admin.web.domain.vo.PageResultVO;
import com.lvmama.tnt.bms.admin.web.mapper.PushFailureMapper;
import com.lvmama.tnt.bms.admin.web.service.ManualPushService;
import com.lvmama.tnt.bms.admin.web.service.PushFailureService;
import com.lvmama.tnt.bms.admin.web.util.PageUtil;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class PushFailureServiceImpl implements PushFailureService {
    private Logger logger = LoggerFactory.getLogger(PushFailureServiceImpl.class);

    @Autowired
    private PushFailureMapper pushFailureMapper;

    @Autowired
    private ManualPushService manualPushService;

    @Override
    public PageResultVO<List<PushFailureDO>> findByPage(PushFailureDO pushFailureDO, int pageNo, int pageSize) {
        PageResultVO<List<PushFailureDO>> resultVo = new PageResultVO<List<PushFailureDO>>();
        long totalCount = pushFailureMapper.totalCount(pushFailureDO);
        long totalPage = PageUtil.calculateTotalPage(totalCount, pageSize);
        PageHelper.startPage(pageNo, pageSize);

        List<PushFailureDO> list = pushFailureMapper.findByPage(pushFailureDO);
        resultVo.setResult(list);
        resultVo.setTotalCount(totalCount);
        resultVo.setTotalPage(totalPage);
        resultVo.setPageNo(pageNo);
        return resultVo;
    }

    @Override
    public boolean delete(Integer id) {
        logger.info("delete push failure id = {}", id);
        return pushFailureMapper.delete(id) > 0;
    }

    @Override
    public boolean send(Integer id) {
        logger.info("retry send push failure id = {}", id);
        try {
            PushFailureDO pushFailureDO = pushFailureMapper.findById(id);
            MessageVO messageVO = new MessageVO();
            messageVO.setTokens(pushFailureDO.getToken());
            messageVO.setMsgType(pushFailureDO.getMsgType());
            messageVO.setMsgId(pushFailureDO.getMsgId());
            messageVO.setMaxRetry(String.valueOf(pushFailureDO.getMaxRetry()));
            messageVO.setReplaceOnExist("false");
            String content = pushFailureDO.getMsgContent();
            messageVO.setPayload(StringUtils.isNotEmpty(content) ? JSON.parseArray(content).get(0) : null);
            manualPushService.pushNews(messageVO);
            pushFailureMapper.delete(id);
        } catch (Exception e) {
            logger.error("", e);
            throw new BusinessException("重新推送失败");
        }
        return true;
    }
}

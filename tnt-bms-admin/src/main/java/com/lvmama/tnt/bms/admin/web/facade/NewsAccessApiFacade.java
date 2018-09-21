package com.lvmama.tnt.bms.admin.web.facade;

import com.lvmama.tnt.bms.admin.web.controller.BaseController;
import com.lvmama.tnt.bms.api.domain.NewsAccessDTO;
import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsAccessDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.ResponseVO;
import com.lvmama.tnt.bms.admin.web.service.NewsAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@RestController
public class NewsAccessApiFacade extends BaseController {
    private Logger logger = LoggerFactory.getLogger(NewsAccessApiFacade.class);
    @Autowired
    private NewsAccessService newsAccessService;

    @RequestMapping(value = "/config/access/editNews", method = RequestMethod.POST)
    public ResponseVO newsTypeEdit(@RequestBody NewsAccessDTO newsAccessDTO) {
        try {
            newsAccessService.updateForAPI(newsAccessDTO);
        } catch (BusinessException e) {
            logger.warn(e.getMessage(),e);
            return returnFailed(e.getMessage());
        }
        return returnSuccess("修改成功");
    }

    @RequestMapping(value = "/config/access/addNews", method = RequestMethod.POST)
    public ResponseVO newsTypeAdd(@RequestBody NewsAccessDTO newsAccessDTO) {
        String token = null;
        try {
            token = newsAccessService.insertForAPI(newsAccessDTO);
        } catch (BusinessException e) {
            return returnFailed(e.getMessage());
        }
        return returnSuccess("接入成功", token);
    }

    @RequestMapping("/config/access/find/{id}")
    public ResponseVO findAccessById(@PathVariable Long id) {
        ResponseVO responseVO = returnSuccess();
        try {
            NewsAccessDO newsAccessDO = newsAccessService.findById(id);
            NewsAccessDTO newsAccessDTO = new NewsAccessDTO();
            BeanUtils.copyProperties(newsAccessDO,newsAccessDTO);
            responseVO.setResult(newsAccessDTO);
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return returnFailed(e.getMessage());
        }
        return responseVO;
    }

    @RequestMapping("/config/access/find/token/{token}")
    public ResponseVO findAccessByToken(@PathVariable String token) {
        ResponseVO responseVO = returnSuccess();
        try {
            NewsAccessDO newsAccessDO = newsAccessService.findByToken(token);
            NewsAccessDTO newsAccessDTO = new NewsAccessDTO();
            BeanUtils.copyProperties(newsAccessDO,newsAccessDTO);
            responseVO.setResult(newsAccessDTO);
        } catch (BusinessException e) {
            logger.info(e.getMessage());
            return returnFailed(e.getMessage());
        }
        return responseVO;
    }
}

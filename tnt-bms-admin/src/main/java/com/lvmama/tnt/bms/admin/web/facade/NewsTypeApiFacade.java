package com.lvmama.tnt.bms.admin.web.facade;

import com.lvmama.tnt.bms.admin.web.controller.BaseController;
import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.ResponseVO;
import com.lvmama.tnt.bms.admin.web.service.NewsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 *
 */
@RestController
@RequestMapping("/api")
public class NewsTypeApiFacade extends BaseController{

    @Autowired
    private NewsTypeService newsTypeService;


    @RequestMapping(value = "/config/type/addNews", method = RequestMethod.POST)
    public ResponseVO newsTypeAdd(@RequestBody NewsTypeDO newsTypeDO) {
        try {
            newsTypeService.insert(newsTypeDO);
        } catch (BusinessException e) {
            return returnFailed(e.getMessage());
        }
        return returnSuccess("保存成功");
    }

    @RequestMapping("/config/type/list")
    public ResponseVO newsTypeList(String newsType) {
        ResponseVO responseVO = returnSuccess();
        List<NewsTypeDO> types = newsTypeService.findTypeListByParam(newsType);
        responseVO.setResult(types);
        return responseVO;
    }

}

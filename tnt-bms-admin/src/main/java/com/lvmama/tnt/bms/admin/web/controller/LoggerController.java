package com.lvmama.tnt.bms.admin.web.controller;

import com.lvmama.tnt.bms.admin.web.domain.vo.LogVO;
import com.lvmama.tnt.bms.admin.web.manage.controller.AbstractController;
import com.lvmama.tnt.bms.admin.web.search.TntBmsLogService;
import com.lvmama.tnt.bms.api.domain.NewsAccessDTO;
import com.lvmama.tnt.bms.api.domain.NewsTypeDTO;
import com.lvmama.tnt.bms.api.domain.ResponseDTO;
import com.lvmama.tnt.bms.api.service.IAccessService;
import com.lvmama.tnt.bms.api.service.INewsTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/logger")
public class LoggerController extends AbstractController{
    private Logger logger = LoggerFactory.getLogger(LoggerController.class);
    @Autowired
    private INewsTypeService newsTypeService;

    @Autowired
    private IAccessService accessService;

    @Autowired
    private TntBmsLogService tntBmsLogService;

    @RequestMapping("/index")
    public String index() {
        return "logger/index";
    }

    @ResponseBody
    @RequestMapping("/query")
    public ResponseDTO queryLog(@RequestBody LogVO.Request request) {
       logger.info("query by : {}", request);
        if (request.getPageNo() > 20) {
            return returnError("请选择条件精确查询");
        }
        return tntBmsLogService.searchLog(request);
    }

    @ResponseBody
    @RequestMapping("/queryById")
    public ResponseDTO<LogVO.Response> queryById(String id) {
        return tntBmsLogService.queryById(id);
    }

    @ResponseBody
    @RequestMapping("/types")
    public ResponseDTO<List<NewsTypeDTO>> pushTypes() {
        return newsTypeService.findAllNewsType();
    }

    @ResponseBody
    @RequestMapping("/access")
    public ResponseDTO<List<NewsAccessDTO>> access(String keyword) {
        return accessService.fuzzyByName(keyword);
    }



}

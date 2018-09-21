package com.lvmama.tnt.bms.admin.web.controller;

import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;
import com.lvmama.tnt.bms.admin.web.domain.define.Constants;
import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;
import com.lvmama.tnt.bms.admin.web.domain.po.ConvertDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.PageResultVO;
import com.lvmama.tnt.bms.admin.web.domain.vo.ResponseVO;
import com.lvmama.tnt.bms.admin.web.service.ConvertManageService;
import com.lvmama.tnt.bms.admin.web.util.PageUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
@Controller
public class ConvertManageController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(ConvertManageController.class);

    @Autowired
    private ConvertManageService convertManageService;

    @RequestMapping("/config/convert/list")
    public String toPusher(Model model, ConvertDO convertDO, HttpServletRequest request) {
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = StringUtils.isNotEmpty(pageNoStr) ? Integer.parseInt(pageNoStr) : 1;
        List<ConvertDO> list = convertManageService.findByPage(convertDO, pageNo, Constants.PAGE_SIZE);
        long totalCount = convertManageService.totalCount(convertDO);

        PageResultVO<List<ConvertDO>> resultVo = new PageResultVO<List<ConvertDO>>();
        resultVo.setPageNo(pageNo);
        resultVo.setTotalCount(totalCount);
        resultVo.setTotalPage(PageUtil.calculateTotalPage(totalCount,Constants.PAGE_SIZE));
        resultVo.setResult(list);
        model.addAttribute("resultVo", resultVo);
        model.addAttribute("pageinationd", PageUtil.getPageContent(request, resultVo));
        return "news/convert_manager_list";
    }

    @RequestMapping("/config/convert/addConvert")
    public String addPusher(Model model, ConvertDO convertDO, HttpServletRequest request) {
        if (convertDO != null && !StringUtils.isEmpty(convertDO.getRequestMap())) {
            convertDO.setRequestMap(StringEscapeUtils.unescapeHtml(convertDO.getRequestMap()));
        }
        if (convertDO != null && !StringUtils.isEmpty(convertDO.getResponseMap())) {
            convertDO.setResponseMap(StringEscapeUtils.unescapeHtml(convertDO.getResponseMap()));
        }
        if (convertDO != null && !StringUtils.isEmpty(convertDO.getHttpHeader())) {
            convertDO.setHttpHeader(StringEscapeUtils.unescapeHtml(convertDO.getHttpHeader()));
        }
        if (convertDO.getId() != null) {
            convertManageService.update(convertDO);
        } else {
            convertManageService.insert(convertDO);
        }
        return "redirect:list";
    }

    @RequestMapping("/config/convert/toAdd_{convertID}")
    public String toAdd(Model model,@PathVariable Integer convertID) {
        ConvertDO convertDO = null;
        if (convertID != null && convertID > 0) {
            convertDO = convertManageService.findByID(convertID);
        }
        if (convertDO == null) {
            convertDO = new ConvertDO();
        }
        model.addAttribute("convertDO", convertDO);
        return "news/convert_manager_add";
    }


    @ResponseBody
    @RequestMapping("/config/convert/delete/{convertID}")
    public ResponseVO deleteNesType(Model model, @PathVariable("convertID") Integer convertID) {
        try {
            convertManageService.delete(convertID);
        } catch (BusinessException e) {
            logger.error(e);
            return returnFailed(e.getMessage());
        }
        return returnSuccess("删除成功");
    }

    @ResponseBody
    @RequestMapping("/config/convert/check/{name}")
    public ResponseVO checkExist(Model model, @PathVariable String name) {
        if(convertManageService.checkExist(name)){
            return returnFailed("该映射器已注册");
        }
        return returnSuccess();
    }
}

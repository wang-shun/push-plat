package com.lvmama.tnt.bms.admin.web.controller;

import com.lvmama.tnt.bms.admin.web.domain.exception.BusinessException;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.PageResultVO;
import com.lvmama.tnt.bms.admin.web.domain.vo.ResponseVO;
import com.lvmama.tnt.bms.admin.web.service.NewsTypeService;
import com.lvmama.tnt.bms.admin.web.domain.define.Constants;
import com.lvmama.tnt.bms.admin.web.util.PageUtil;
import com.lvmama.tnt.bms.admin.web.util.TimeContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author longhr
 * @version 2017/11/1 0001
 */
@Controller
public class NewsTypeController extends BaseController{

    @Autowired
    private NewsTypeService newsTypeService;

    @RequestMapping("/config/type/toAdd")
    public String newsTypeToAdd(Model model, HttpServletRequest request) {
        return "news/news_type_add";
    }

    @ResponseBody
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
    public String newsTypeList(Model model, NewsTypeDO newsTypeDO, HttpServletRequest request) {
        String pageNoStr = request.getParameter("pageNo");
        PageResultVO<List<NewsTypeDO>> resultVo = newsTypeService.findByPage(newsTypeDO,
                StringUtils.isNotEmpty(pageNoStr) ? Integer.parseInt(pageNoStr) : 1, Constants.PAGE_SIZE);
        model.addAttribute("resultVo", resultVo);
        model.addAttribute("pageinationd", PageUtil.getPageContent(request, resultVo));
        return "news/news_type_list";
    }

    @RequestMapping("/config/type/toEdit_{id}")
    public String newsTypeToEidt(Model model, @PathVariable("id") Long id) {
        model.addAttribute("newsTypeDO", newsTypeService.findById(id));
        return "news/news_type_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/config/type/editNews", method = RequestMethod.POST)
    public ResponseVO newsTypeEdit(@RequestBody NewsTypeDO newsTypeDO) {
        try {
            newsTypeService.update(newsTypeDO);
        } catch (BusinessException e) {
            return returnFailed(e.getMessage());
        }
        return returnSuccess("修改成功");
    }

    @ResponseBody
    @RequestMapping("/config/type/delete/{typeID}")
    public ResponseVO deleteNesType(Model model, @PathVariable("typeID") Long typeID) {
        try {
            newsTypeService.delete(typeID);
        } catch (BusinessException e) {
            return returnFailed(e.getMessage());
        }
        return returnSuccess("删除成功");
    }

    @ResponseBody
    @RequestMapping("/config/type/check/{name}")
    public ResponseVO checkExist(Model model, @PathVariable String name) {
        if (newsTypeService.existName(name)){
            return returnFailed("改消息类型已经存在！");
        }
        return returnSuccess();
    }

}

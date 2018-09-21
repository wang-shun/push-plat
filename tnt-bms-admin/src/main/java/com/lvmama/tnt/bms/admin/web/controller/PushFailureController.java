package com.lvmama.tnt.bms.admin.web.controller;

import com.lvmama.tnt.bms.admin.web.domain.define.Constants;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import com.lvmama.tnt.bms.admin.web.domain.po.PushFailureDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.PageResultVO;
import com.lvmama.tnt.bms.admin.web.domain.vo.ResponseVO;
import com.lvmama.tnt.bms.admin.web.service.PushFailureService;
import com.lvmama.tnt.bms.admin.web.util.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/config/failure")
public class PushFailureController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(PushFailureController.class);

    @Autowired
    private PushFailureService pushFailureService;

    @RequestMapping("/list")
    public String list(HttpServletRequest request, Model model, PushFailureDO pushFailureDO) {
        String pageNoStr = request.getParameter("pageNo");
        PageResultVO<List<PushFailureDO>> resultVo = pushFailureService.findByPage(pushFailureDO,
                StringUtils.isNotEmpty(pageNoStr) ? Integer.parseInt(pageNoStr) : 1, Constants.PAGE_SIZE);
        model.addAttribute("resultVo", resultVo);
        model.addAttribute("pageinationd", PageUtil.getPageContent(request, resultVo));
        return "news/news_failure_list";
    }

    @ResponseBody
    @RequestMapping("/delete/{id}")
    public ResponseVO<String> delete(@PathVariable Integer id) {
        ResponseVO responseVO = returnSuccess();
        try {
            pushFailureService.delete(id);
        } catch (Exception e) {
            logger.error("", e.getMessage());
            return returnFailed(e.getMessage());
        }
        return responseVO;
    }

    @ResponseBody
    @RequestMapping("/send/{id}")
    public ResponseVO<String> send(@PathVariable Integer id) {
        ResponseVO responseVO = returnSuccess();
        try {
            pushFailureService.send(id);
        } catch (Exception e) {
            logger.error("", e.getMessage());
            return returnFailed(e.getMessage());
        }
        return responseVO;
    }
}

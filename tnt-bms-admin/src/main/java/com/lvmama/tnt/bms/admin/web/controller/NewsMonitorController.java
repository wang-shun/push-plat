package com.lvmama.tnt.bms.admin.web.controller;

import com.lvmama.tnt.bms.admin.web.domain.vo.RequestVO;
import com.lvmama.tnt.bms.admin.web.domain.vo.ResponseVO;
import com.lvmama.tnt.bms.admin.web.service.NewsMonitorService;
import com.lvmama.tnt.bms.admin.web.util.BmsDateUtils;
import com.lvmama.tnt.bms.admin.web.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * @author longhr
 * @version 2017/12/4 0004
 */
@Controller
public class NewsMonitorController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(NewsMonitorController.class);

    @Autowired
    private NewsMonitorService newsMonitorService;

    @RequestMapping("/chart/warning/news")
    public String newsEarlyWarning(Model model, HttpServletRequest request) {
        RequestVO vo = new RequestVO();
        vo.setStartTime(BmsDateUtils.getFourHoursAgoTime());
        vo.setEndTime(DateUtil.convert(new Date(),BmsDateUtils.DATE_FORMAT));

        model.addAttribute("requestVo", vo);
        return "chart/news_early_warning";
    }

    @ResponseBody
    @RequestMapping("/chart/warning/newsData")
    public ResponseVO newsEarlyWarningData(Model model, HttpServletRequest request, @RequestBody RequestVO requestVO) {
        ResponseVO responseVO = null;
        logger.info("查询参数：{}",requestVO);
        try {
            if (requestVO == null || StringUtils.isEmpty(requestVO.getStartTime()) || StringUtils.isEmpty(requestVO.getEndTime())) {
                logger.info("查询参数为空:{}",requestVO);
                return returnFailed("查询参数为空");
            }
            responseVO = returnSuccess();
            responseVO.setResult(newsMonitorService.findByInterval(requestVO));
        } catch (Exception e) {
            responseVO = returnFailed(e.getMessage());
        }
        return  responseVO;
    }

}

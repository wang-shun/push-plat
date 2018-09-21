package com.lvmama.tnt.bms.admin.web.controller;

import com.alibaba.dubbo.common.json.JSONArray;
import com.alibaba.fastjson.JSON;
import com.lvmama.bms.core.domain.Message;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.MessageVO;
import com.lvmama.tnt.bms.admin.web.service.ManualPushService;
import com.lvmama.tnt.bms.admin.web.service.NewsTypeService;
import com.lvmama.tnt.bms.admin.web.util.FastJsonUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 *
 */
@Controller
public class ManualPushController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(ManualPushController.class);

    @Autowired
    private ManualPushService manualPushService;
    @Autowired
    private NewsTypeService newsTypeService;

    @RequestMapping("/config/push/toManual")
    public String toManualPush(Model model) {
        List<String> list = newsTypeService.findTypeList();
        model.addAttribute("types", list);
        return "news/manual_push";
    }

    @RequestMapping("/config/push/pushNews")
    public String pushNews(Model model, @ModelAttribute MessageVO message) {
        logger.info("手动推送消息：{}", FastJsonUtil.toJsonString(message));
        List<String> list = newsTypeService.findTypeList();
        model.addAttribute("types", list);
        if (message != null) {
            if (!StringUtils.isEmpty(message.getTokens())) {
                message.setTokens(StringEscapeUtils.unescapeHtml(message.getTokens()));
            }
            String payload = (String) message.getPayload();
            if (!StringUtils.isEmpty(payload)) {
                message.setPayload(StringEscapeUtils.unescapeHtml(payload));
            }
            message.setPayload(JSON.parse((String) message.getPayload()));
        }
        manualPushService.pushNews(message);
        return "news/manual_push";
    }

}

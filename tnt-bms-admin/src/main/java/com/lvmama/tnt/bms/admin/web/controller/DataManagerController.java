package com.lvmama.tnt.bms.admin.web.controller;

import com.alibaba.fastjson.JSON;
import com.lvmama.bms.core.cluster.sync.SyncEvent;
import com.lvmama.comm.pet.po.pub.TaskResult;
import com.lvmama.tnt.bms.admin.web.domain.po.NewsTypeDO;
import com.lvmama.tnt.bms.admin.web.domain.po.ReceiverMonitorDO;
import com.lvmama.tnt.bms.admin.web.domain.vo.ResponseVO;
import com.lvmama.tnt.bms.admin.web.sendMail.SendMailTask;
import com.lvmama.tnt.bms.admin.web.service.NewsAccessService;
import com.lvmama.tnt.bms.admin.web.service.NewsMonitorService;
import com.lvmama.tnt.bms.admin.web.service.NewsTypeService;
import com.lvmama.tnt.bms.admin.web.service.SendConfigService;
import com.lvmama.tnt.bms.admin.web.util.DateUtil;
import com.lvmama.tnt.bms.admin.web.util.FastJsonUtil;
import io.protostuff.Request;
import org.apache.activemq.command.ActiveMQTopic;
import org.codehaus.plexus.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
@RestController
@RequestMapping("/data")
public class DataManagerController extends BaseController {

    @Autowired
    private NewsTypeService newsTypeService;

    @Autowired
    private NewsAccessService newsAccessService;

    @Autowired
    private SendConfigService sendConfigService;

    @Autowired
    private NewsMonitorService newsMonitorService;

    @Autowired
    private SendMailTask sendMailTask;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    @Qualifier("queueConvert")
    private ActiveMQTopic queueConvert;

    @RequestMapping("/clearMsgToken/{tokenId}")
    public ResponseVO clearMsgToken(@PathVariable Long tokenId) {
        int result = 0;
        Map<String, String> paramsMap = new HashMap<>();
        List<NewsTypeDO> types = newsTypeService.findTypeListByParam(paramsMap);
        if (types != null && types.size() > 0) {
            for (NewsTypeDO typeDO : types) {
                result += newsAccessService.deleteMessageToken(typeDO.getId(), Arrays.asList(new Long[]{tokenId}));
            }
        }
        return returnSuccess("success", result);
    }

    @RequestMapping("/flushAll")
    public ResponseVO flushRedisAll() {
        try {
            SyncEvent event = new SyncEvent(SyncEvent.ObjectType.FLUSH_REDIS, SyncEvent.EventType.All, null);
            sendConfigService.sendMessage(event);
        } catch (Exception e) {
            return returnFailed(ExceptionUtils.getFullStackTrace(e));
        }
        return returnSuccess("success");
    }

    @RequestMapping("/countSend")
    public TaskResult countSend() {
        TaskResult taskResult = new TaskResult();
        try {
            Date yesterday = DateUtil.addDayToDate(new Date(), -1);
            Long startTime = DateUtil.getStartDatetime(yesterday).getTime();
            Long endTime = DateUtil.getEndDatetime(yesterday).getTime();
            final List<ReceiverMonitorDO> list = newsMonitorService.countSendGroupByToken(startTime, endTime);
            sendMailTask.submit(list);

            taskResult.setRunStatus(TaskResult.RUN_STATUS.SUCCESS);
            taskResult.setResult("成功");
        } catch (Exception e) {
            taskResult.setRunStatus(TaskResult.RUN_STATUS.FAILED);
            taskResult.setResult("失败");
        }
        return taskResult;
    }

    @RequestMapping("/changeParams")
    public ResponseVO changeParams(HttpServletRequest request) {
        Map<String, Object> paramsMap = new HashMap<>();
        Enumeration em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = (String) em.nextElement();
            Object value = request.getParameter(name);
            paramsMap.put(name, value);
        }

        SyncEvent message = new SyncEvent();
        message.setObjectType(SyncEvent.ObjectType.CONFIG);
        message.setEventType(SyncEvent.EventType.Update);
        message.setAddition(FastJsonUtil.toJsonString(paramsMap));
        jmsMessagingTemplate.convertAndSend(queueConvert,FastJsonUtil.toJsonString(message));
        return returnSuccess();
    }

}

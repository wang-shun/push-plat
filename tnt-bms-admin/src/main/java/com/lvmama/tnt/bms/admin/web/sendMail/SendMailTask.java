package com.lvmama.tnt.bms.admin.web.sendMail;

import com.lvmama.bms.alarm.email.EmailTableUtils;
import com.lvmama.bms.core.commons.utils.StringUtils;
import com.lvmama.tnt.bms.admin.web.domain.po.ReceiverMonitorDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
@Service
public class SendMailTask {

    @Autowired
    private MailClient mailClient;

    private ExecutorService service = Executors.newSingleThreadExecutor();
    private static final Float FAIL_RATE = 0.6F;
    private static final String header = "接收方Token,接收方名称,接收数量,发送数量,到达数量,失败数量,失败率";

    public void submit(final List<ReceiverMonitorDO> originData) {
        NumberFormat numberFormat = NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(2);
        service.submit(new Runnable() {
            @Override
            public void run() {
                List<List<Object>> info = new ArrayList<List<Object>>();
                for (ReceiverMonitorDO rd : originData) {
                    if (StringUtils.isNotEmpty(rd.getToken()) && rd.getFailCount() != null && rd.getSendCount() != null) {
                        if (rd.getFailCount() / rd.getSendCount() > FAIL_RATE) {
                            info.add(Arrays.asList(
                                    (Object) rd.getToken(), rd.getName(),
                                    rd.getReceiveCount(), rd.getSendCount(),
                                    rd.getReachCount(), rd.getFailCount(),numberFormat.format(Double.valueOf(rd.getFailCount()) / Double.valueOf(rd.getSendCount()))));
                        }
                    }
                }
                String content = EmailTableUtils.createTable("商家推送失败率统计", header.split(","), info);
                EmailEntity entity = new EmailEntity();
                entity.setSubject("商家推送失败率统计");
                entity.setContent(content.toString());
                mailClient.sendHtmlMail(entity);
            }
        });
    }


}

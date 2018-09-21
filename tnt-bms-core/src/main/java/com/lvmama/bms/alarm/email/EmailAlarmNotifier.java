package com.lvmama.bms.alarm.email;

import com.lvmama.bms.alarm.AbstractAlarmNotifier;
import com.lvmama.bms.alarm.AlarmNotifyException;
import com.lvmama.bms.core.AppContext;
import com.lvmama.bms.core.cluster.Config;
import com.lvmama.bms.core.constant.ExtConfig;
import com.lvmama.bms.core.logger.Logger;
import com.lvmama.bms.core.logger.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Robert HG (254963746@qq.com)  on 2/17/16.
 */
public class EmailAlarmNotifier extends AbstractAlarmNotifier<EmailAlarmMessage> {

    private Logger logger = LoggerFactory.getLogger(EmailAlarmNotifier.class);

    private final MailManager mailManager;
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    public EmailAlarmNotifier(AppContext appContext) {
        this.asyncSend = true;
        this.mailManager = getMailManager(appContext.getConfig());
    }

    public EmailAlarmNotifier(AppContext appContext, boolean asyncSend) {
        this.asyncSend = asyncSend;
        this.mailManager = getMailManager(appContext.getConfig());
    }
    
    public EmailAlarmNotifier(Config config) {
        this.asyncSend = true;
    	this.mailManager = getMailManager(config);
    }

    public EmailAlarmNotifier(Config config, boolean asyncSend) {
        this.asyncSend = asyncSend;
        this.mailManager = getMailManager(config);
    }
    
    private MailManager getMailManager(Config config) {
        String host = config.getParameter(ExtConfig.MAIL_SMTP_HOST);
        String port = config.getParameter(ExtConfig.MAIL_SMTP_PORT);
        String userName = config.getParameter(ExtConfig.MAIL_USERNAME);
        String password = config.getParameter(ExtConfig.MAIL_PASSWORD);
        String adminAddress = config.getParameter(ExtConfig.MAIL_ADMIN_ADDR);
        boolean sslEnabled = config.getParameter(ExtConfig.MAIL_SSL_ENABLED, true);
        return new SMTPMailManagerImpl(host, port, userName, password, adminAddress, sslEnabled);
    }
    

    @Override
    protected void doNotice(EmailAlarmMessage message) {
        try {
            mailManager.send(message.getTo(), message.getTitle(), message.getMsg());
        } catch (Exception e) {
            throw new AlarmNotifyException("EmailAlarmNotifier send error", e);
        }
    }

    @Override
    protected void asyncNotice(final EmailAlarmMessage message) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mailManager.send(message.getTo(), message.getTitle(), message.getMsg());
                } catch (Exception e) {
                    logger.error("tip=EmailAlarmNotifier send error|exception=", e);
                }
            }
        });
    }
}

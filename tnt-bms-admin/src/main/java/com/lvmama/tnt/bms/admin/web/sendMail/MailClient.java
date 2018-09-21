package com.lvmama.tnt.bms.admin.web.sendMail;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 *
 */
@Component
public class MailClient {

    private Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;


    public void sendSimpleMail(EmailEntity emailEntity) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(mailProperties.getProperties().get("from"));
            simpleMailMessage.setTo(mailProperties.getProperties().get("to").split(";"));
            simpleMailMessage.setSubject(emailEntity.getSubject());
            simpleMailMessage.setText(emailEntity.getContent());
            javaMailSender.send(simpleMailMessage);
            logger.error("MailClient send SimpleMail Success");
        } catch (Exception e) {
            logger.error("MailClient send SimpleMail Exception", e);
        }
    }

    public void sendHtmlMail(EmailEntity emailEntity) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(mailProperties.getProperties().get("from"));
            helper.setTo(mailProperties.getProperties().get("to").split(";"));
            helper.setSubject(emailEntity.getSubject());
            helper.setText(emailEntity.getContent(), true);
            javaMailSender.send(message);
            logger.info("html格式邮件发送成功");
        }catch (Exception e){
            logger.error("html格式邮件发送失败", e);
        }
    }


}

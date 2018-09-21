package com.lvmama.bms.alarm.email;

import com.lvmama.bms.core.commons.utils.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;
import java.util.Random;

public class SMTPMailManagerImpl implements MailManager {

    private String userName;

    private String password;

    private String[] adminAddresses;

    private Properties properties;

    public SMTPMailManagerImpl(String host, String userName, String password,
                               String adminAddress, boolean sslEnabled) {
        this(host, "", userName, password, adminAddress, sslEnabled);
    }

    public SMTPMailManagerImpl(String host, String port, String userName, String password,
                               String adminAddress, boolean sslEnabled) {
        this.userName = userName;
        this.password = password;
        this.adminAddresses = adminAddress.split(",");

        properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        if (!StringUtils.isEmpty(host)) {
            properties.setProperty("mail.smtp.host", host);
        }
        if (!StringUtils.isEmpty(port)) {
            properties.setProperty("mail.smtp.port", port);
        }
        if (!StringUtils.isEmpty(userName)) {
            properties.setProperty("mail.smtp.security", "true");
        } else {
            properties.setProperty("mail.smtp.security", "false");
        }

        if (sslEnabled) {
            properties.setProperty("mail.smtp.starttls.enable", "true");
        }
    }

    private Authenticator getAuthenticator() {
        if (!StringUtils.isEmpty(userName)) {
            return new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            };
        } else {
            return null;
        }
    }

    private Authenticator getAuthenticator(final String userName) {
        if (!StringUtils.isEmpty(userName)) {
            return new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            };
        } else {
            return null;
        }
    }

    public void send(String to, String title, String message) throws Exception {

        String adminAddress  = adminAddresses[new Random().nextInt(adminAddresses.length)];

        Session session = Session.getInstance(properties, getAuthenticator(adminAddress));
        // Create a default MimeMessage object.
        MimeMessage mimeMessage = new MimeMessage(session);
        // Set From: header field of the header.
        mimeMessage.setFrom(new InternetAddress(adminAddress));
        // Set To: header field of the header.
        mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        // Set Subject: header field
        mimeMessage.setSubject(title);
        // Now set the actual message
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(message, "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);
        mimeMessage.setContent(multipart);
        // Send message
        Transport.send(mimeMessage);
    }
}
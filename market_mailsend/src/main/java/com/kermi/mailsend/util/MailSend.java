package com.kermi.mailsend.util;

import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;


@Component
public class MailSend {

    private final String host = "smtp.sina.com";
    private final String sendUser = "kermisweet@sina.com";
    private final String sendPwd = "0ac87770402fd6bd";

    public void sendMail(String email, String code) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.debug", "true");
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.host", host);
            props.setProperty("mail.transport.protocol", "stmp");

            String smtpPort = "465";
            props.setProperty("mail.smtp.port", smtpPort);
            props.setProperty("mail.smtp.ssl.enable", "true");

            Session session = Session.getDefaultInstance(props);
            session.setDebug(false);
            Message msg = new MimeMessage(session);

            msg.setSubject(emailSubject());
            msg.setText(emailContent(code));
            msg.setSentDate(new Date());
            msg.setFrom(new InternetAddress(sendUser));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            msg.saveChanges();
            Transport transport = session.getTransport("smtp");
            transport.connect(host, sendUser, sendPwd);
            transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
            transport.close();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String emailSubject() {
        StringBuilder sb = new StringBuilder("cloudproject 验证码");
        return sb.toString();

    }

    public static String emailContent(String code) {
        StringBuilder sb =new StringBuilder("你的验证码为");
        sb.append(code);
        sb.append("请在5分钟之内使用，过期后请重新获取");
        sb.append(new Date());
        return sb.toString();
    }

}

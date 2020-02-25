package com.kermi.mailsend.rabbitmq;

import com.kermi.mailsend.util.MailSend;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "cloudprojectmail")
public class MailCostomer {

    @Autowired
    private MailSend mailSend;

    @RabbitHandler
    public void emailSend(Map<String, String> map) {
        mailSend.sendMail(map.get("email"), map.get("code"));
    }
}

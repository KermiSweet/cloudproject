package com.kermi.base.service.impl;

import com.kermi.base.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.RedisUtil;
import util.VerifyCodeUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {

    private Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisUtil redisUtil;

    private final String REDIS_EMAIL_CODE_PREFIX = "EMAIL_CODE_";
    private final String REDIS_SET_INFO = "Redis存入值:";

    @Override
    public String sendEmailToRbq(String email) {
        Map<String, String> emailAndMsg = new HashMap<>();
        //得到随机验证码
        String randomCode = VerifyCodeUtil.generateVerifyCode(6);
        emailAndMsg.put("email", email);
        emailAndMsg.put("code", randomCode);
        rabbitTemplate.convertAndSend("cloudprojectmail", emailAndMsg);
        return randomCode;
    }

    @Override
    public void saveCodeToRedis(String sessionId, String randomcode) {
        redisUtil.set(REDIS_EMAIL_CODE_PREFIX + sessionId, randomcode);
        //设置过期时间
        redisUtil.expire(REDIS_EMAIL_CODE_PREFIX + sessionId, 5, TimeUnit.MINUTES);
        logger.info(new Exception().getStackTrace()[0].getMethodName() + REDIS_SET_INFO + REDIS_EMAIL_CODE_PREFIX + sessionId + ":" + randomcode + "剩余有效时间" + redisUtil.getExpire(REDIS_EMAIL_CODE_PREFIX + sessionId));
    }
}

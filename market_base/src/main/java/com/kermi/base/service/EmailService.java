package com.kermi.base.service;

import java.util.Map;

public interface EmailService {
    /**
     * 发送要发送邮件的消息到RabbitMQ
     * @param email
     * 返回随机验证码
     */
    String sendEmailToRbq(String email);

    /**
     * 保存验证码到Redis缓存中，
     * @param sessionId
     * @param randomcode
     */
    void saveCodeToRedis(String sessionId, String randomcode);
}

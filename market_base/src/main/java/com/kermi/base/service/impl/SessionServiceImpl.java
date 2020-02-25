package com.kermi.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kermi.base.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.RedisUtil;
import util.SessionAttributes;

import java.util.concurrent.TimeUnit;

@Service
public class SessionServiceImpl implements SessionService {

    private Logger logger = LoggerFactory.getLogger(SessionService.class);

    private String SESSION_REDIS_PREFIX = "SESSION_";

    private String REDIS_GET_INFO = "查询Redis数据库:";
    private String REDIS_SET_INFO = "写入Redis数据库:";

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void writeSessionAttributesToRedis(SessionAttributes attributes) {
        redisUtil.set(SESSION_REDIS_PREFIX + attributes.getSessionId(), JSONObject.toJSONString(attributes));
        redisUtil.expire(SESSION_REDIS_PREFIX+attributes.getSessionId(), 30, TimeUnit.MINUTES);
        logger.info(new Exception().getStackTrace()[0].getMethodName() + ":" + REDIS_SET_INFO + ":" + attributes.getSessionId());
    }

    @Override
    public SessionAttributes getSessionAttributesFromRedis(String sessionID) {
        String sessionAttributes = redisUtil.get(SESSION_REDIS_PREFIX + sessionID);
        String methodName = new Exception().getStackTrace()[0].getMethodName();
        logger.info(methodName + REDIS_GET_INFO + ":" + SESSION_REDIS_PREFIX + sessionID);
        if (sessionAttributes != null) {
            logger.info(methodName + REDIS_GET_INFO + ":" + SESSION_REDIS_PREFIX + sessionID + "OK");
            return JSONObject.parseObject(sessionAttributes, SessionAttributes.class);
        }
        return null;
    }
}

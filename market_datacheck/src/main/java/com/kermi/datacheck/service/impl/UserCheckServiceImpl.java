package com.kermi.datacheck.service.impl;

import com.kermi.common.mapper.UserMapper;
import com.kermi.common.pojo.User;
import com.kermi.datacheck.service.UserCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.RedisUtil;

@Service
public class UserCheckServiceImpl implements com.kermi.datacheck.service.UserCheckService {

    private Logger logger = LoggerFactory.getLogger(UserCheckService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    private final String REDIS_EMAIL_CODE_PREFIX = "EMAIL_CODE_";

    private String REDIS_EMAIL_SET_NAME = "EMAILSET";
    private String REDIS_USERNAME_SET_NAME = "USERNAMESET";

    private String REDIS_SAVE_INFO = "存入Redis中";
    private String REDIS_GET_INFO = "向Redis发起查询";
    private String REDIS_GET_SUCCESS_INFO = "Redis查询到值:";

    @Override
    public boolean emailExist(String email) {
        //先查询Redis有无此Email
        logger.info(REDIS_GET_INFO + new Exception().getStackTrace()[0].getMethodName() + ":" + email);
        if (redisUtil.sIsMember(REDIS_EMAIL_SET_NAME, email)) {
            logger.info(REDIS_GET_SUCCESS_INFO + email);
            return true;
        }
        //redis中没有查询到，或者无此数据，查询主数据库
        User u = new User();
        u.setEmail(email);
        u = userMapper.selectSelective(u);
        if (u != null) {
            //查询到了，并存储到redis中
            redisUtil.sAdd(REDIS_EMAIL_SET_NAME, u.getEmail());
            logger.info(u.getEmail() + REDIS_SAVE_INFO + "-" + REDIS_EMAIL_SET_NAME);
            return true;
        }
        return false;
    }

    @Override
    public boolean nameExist(String username) {
        //先查询Redis有无此Username
        logger.info(REDIS_GET_INFO + new Exception().getStackTrace()[0].getMethodName() + ":" + username);
        if (redisUtil.sIsMember(REDIS_USERNAME_SET_NAME, username)) {
            return true;
        }
        //redis中没有查询到，或者无此数据，查询主数据库
        User u = new User();
        u.setName(username);
        u = userMapper.selectSelective(u);
        if (u != null) {
            //查询到了，并存储到redis中
            redisUtil.sAdd(REDIS_USERNAME_SET_NAME, u.getName());
            logger.info(u.getEmail() + REDIS_SAVE_INFO + "-" + REDIS_USERNAME_SET_NAME);
            return true;
        }
        return false;
    }

    @Override
    public int codeCheck(String sessionId, String code) {
        Long expire = redisUtil.getExpire(REDIS_EMAIL_CODE_PREFIX + sessionId);
        String methodname = new Exception().getStackTrace()[0].getMethodName() + ":";
        if (expire <= 0) {
            logger.info(methodname + REDIS_GET_INFO + REDIS_EMAIL_CODE_PREFIX + sessionId + "已过期");
            return 2;
        }
        String redisCode = redisUtil.get(REDIS_EMAIL_CODE_PREFIX + sessionId);
        if (code != null && code.equals(redisCode)) {
            //验证通过
            logger.info(methodname + REDIS_GET_SUCCESS_INFO + REDIS_EMAIL_CODE_PREFIX + sessionId + "验证通过");
            return 1;
        }
        logger.info(methodname + REDIS_EMAIL_CODE_PREFIX + sessionId + "验证失败");
        return 0;
    }
}

package com.kermi.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kermi.base.service.UserService;
import com.kermi.common.mapper.UserMapper;
import com.kermi.common.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.RedisUtil;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    private String REDIS_EMAIL_ID = "EMAIL_ID";
    private String REDIS_USERNAME_ID = "USERNAME_ID";
    private String REDIS_HASH_USER = "USER_HASH";

    private String REDIS_GET_INFO = "查询Redis数据库:";
    private String REDIS_SET_INFO = "写入Redis数据库:";

    @Override
    public User login(String username, String email, String pwd) {
        if (validataIsEmpty(pwd)) {
            //用户名为空，直接退出
            return null;
        }
        User user = new User();
        Long id = null;
        if (!validataIsEmpty(username)) {
            user.setName(username);
            user.setPwd(pwd);
            //redis查询USERNAME_ID
            id = Long.parseLong((String) redisUtil.hGet(REDIS_EMAIL_ID, username));
            logger.info(new Exception().getStackTrace()[0].getMethodName() + "-" + REDIS_GET_INFO + REDIS_EMAIL_ID + ":" + username);
            if (id == null) {
                //id未查到的2中情况1. 用户不存在，2. 没有写入Redis数据库
                //查询主数据库
                User chectuser = new User();
                chectuser.setName(username);
                chectuser = userMapper.selectSelective(chectuser);
                if (chectuser == null) {
                    //数据库中不存在该用户
                    return null;
                }
                //存在该用户，将信息存入Redis数据库
                saveUserNameIdToRedis(chectuser);
                saveUserToRedis(chectuser);
                //验证密码是否正确
                if (user.getPwd().equals(chectuser.getPwd())) {
                    return chectuser;
                }
                return null;
            }
        }
        if (!validataIsEmpty(email)) {
            user.setEmail(email);
            user.setPwd(pwd);
            //redis查询EMAIL_ID
            id = Long.parseLong((String) redisUtil.hGet(REDIS_EMAIL_ID, email));
            logger.info(new Exception().getStackTrace()[0].getMethodName() + "-" + REDIS_GET_INFO + REDIS_EMAIL_ID + ":" + email);
            if (id == null) {
                User chectuser = new User();
                chectuser.setEmail(email);
                chectuser = userMapper.selectSelective(chectuser);
                if (chectuser == null) {
                    return null;
                }
                saveEmailIdToRedis(chectuser);
                saveUserToRedis(chectuser);
                if (user.getPwd().equals(chectuser.getPwd())) {
                    return chectuser;
                }
                return null;
            }
        }

        //查询Redis缓存中是否存在用户
        //Redis中放两份，一份是EMAIL_ID,一份是USERNAME_ID
        //id不为null时，从redis中根据id取出相应对象的json
        String outFromRedis = (String) redisUtil.hGet(REDIS_HASH_USER, String.valueOf(id));
        User checkuser = new User();
        if (outFromRedis != null) {
            checkuser =  JSONObject.parseObject(outFromRedis, User.class);
            if (user.getPwd().equals(checkuser.getPwd())) {
                return checkuser;
            }
            return null;
        }

        //Redis中没有查询到，转向主数据库

        checkuser = userMapper.selectSelective(user);
        if (checkuser == null) {
            //不存在此用户
            return null;
        }
        //查询到对象，存入redis数据库
        saveUserToRedis(user);
        //验证对象
        if (user.getPwd().equals(checkuser.getPwd())) {
            return checkuser;
        }

        return null;
    }

    /**
     * 判断字符是否为null或者为空
     *
     * @param str
     * @return
     */
    public boolean validataIsEmpty(String str) {
        if (str != null && !"".equals(str)) {
            return false;
        }
        return true;
    }

    public void saveUserToRedis(User u) {
        String jsonString = JSONObject.toJSONString(u);
        if (redisUtil.hPutIfAbsent(REDIS_HASH_USER, String.valueOf(u.getId()), jsonString)) {
            logger.info(new Exception().getStackTrace()[0].getMethodName() + "-" + REDIS_SET_INFO + REDIS_HASH_USER + ":" + u.getId());
        }
    }

    public void saveUserNameIdToRedis(User u) {
        if (redisUtil.hPutIfAbsent(REDIS_USERNAME_ID, u.getName(), String.valueOf(u.getId()))) {
            logger.info(new Exception().getStackTrace()[0].getMethodName() + "-" + REDIS_SET_INFO + REDIS_USERNAME_ID + ":" + u.getName());
        }
    }

    public void saveEmailIdToRedis(User u) {
        if (redisUtil.hPutIfAbsent(REDIS_EMAIL_ID, u.getEmail(), String.valueOf(u.getId()))) {
            logger.info(new Exception().getStackTrace()[0].getMethodName() + "-" + REDIS_SET_INFO + REDIS_EMAIL_ID + ":" + u.getEmail());
        }
    }


}

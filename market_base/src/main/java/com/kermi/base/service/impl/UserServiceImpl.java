package com.kermi.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kermi.base.service.UserService;
import com.kermi.common.mapper.UserMapper;
import com.kermi.common.pojo.User;
import com.kermi.common.requestBody.RegiestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.IdWorker;
import util.RedisUtil;

@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IdWorker idWorker;

    private String REDIS_EMAIL_ID = "EMAIL_ID";
    private String REDIS_USERNAME_ID = "USERNAME_ID";
    private String REDIS_HASH_USER = "USER_HASH";

    private String REDIS_GET_INFO = "查询Redis数据库:";
    private String REDIS_SET_INFO = "写入Redis数据库:";
    private String REDIS_DELETE_INFO = "删除Redis中的值:";

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
            String stridfromredis = (String) redisUtil.hGet(REDIS_USERNAME_ID, username);
            if (stridfromredis != null) {
                id = Long.parseLong(stridfromredis);
            }
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
            String stridfromredis = (String) redisUtil.hGet(REDIS_EMAIL_ID, email);
            if (stridfromredis != null) {
                id = Long.parseLong(stridfromredis);
            }

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
            checkuser = JSONObject.parseObject(outFromRedis, User.class);
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

    @Override
    public User regiest(RegiestBody regiestBody) {
        //由regiestBody对象转换成pojo类User
        User u = new User(idWorker.nextId(), regiestBody.getUsername(), regiestBody.getEmail(), regiestBody.getPwd());
        //写入主Mysql数据库
        int insert = userMapper.insert(u);
        if (insert != 1) {
            //插入失败,事务关闭
            return null;
        }
        //插入成功，写入缓存数据库USERNAMESET,EMAILSET,USER_HASH(主数据库插入成功后,登录的时候会自动写入)
        return u;
    }

    @Override
    public boolean resetPassword(RegiestBody regiestBody) {
        if (regiestBody.getPwd() == null) {
            return false;
        }
        User u = new User();
        u.setEmail(regiestBody.getEmail());
        //先从缓冲中获取用户对象
        Long id = null;
        String methodName = new Exception().getStackTrace()[0].getMethodName();
        String strid = null;
        strid = (String) redisUtil.hGet(REDIS_EMAIL_ID, regiestBody.getEmail());
        if (strid != null) {
            id = Long.parseLong(strid);
        }
        if (id == null) {
            logger.info(methodName + REDIS_GET_INFO + REDIS_EMAIL_ID + "faild");
            u = userMapper.selectSelective(u);
            id = u.getId();
        }
        String json = (String) redisUtil.hGet(REDIS_HASH_USER, String.valueOf(id));
        if (json == null) {
            logger.info(methodName + REDIS_GET_INFO + REDIS_HASH_USER + "faild");
            u = userMapper.selectSelective(u);
        }

        if (u == null) {
            //数据库中无法查找到改用户
            return false;
        }
        u.setPwd(regiestBody.getPwd());
        int res = userMapper.updateByPrimaryKeySelective(u);
        if (res != 0) {
            //从Redis缓存中删除原来的用户数据
            redisUtil.hDelete(REDIS_HASH_USER, String.valueOf(u.getId()));
            logger.info(new Exception().getStackTrace()[0].getMethodName() + REDIS_DELETE_INFO + REDIS_HASH_USER + "-" + u.getId());
            //修改成功
            return true;
        }
        //修改失败
        return false;
    }

    @Override
    public boolean changePwd(RegiestBody body, String oldPwd) {
        if (body.getPwd() == null) {
            //传入的新密码为空时
            return false;
        }
        //从Redis里面去取id
        Long id = null;
        User u = new User();
        User findfrommysql = null;
        String strid = null;
        strid = (String) redisUtil.hGet(REDIS_EMAIL_ID, body.getEmail());
        if (strid != null) {
            id = Long.parseLong(strid);
        }

        if (id == null) {
            strid = (String) redisUtil.hGet(REDIS_USERNAME_ID, body.getUsername());
            if (strid != null) {
                id = Long.parseLong(strid);
            }
            if (id == null) {
                findfrommysql = getFromMysqlBySelective(body, oldPwd);
                if (findfrommysql == null) {
                    return false;
                }
                redisUtil.hPutIfAbsent(REDIS_USERNAME_ID, findfrommysql.getName(), String.valueOf(findfrommysql.getId()));
                saveUserNameIdToRedis(findfrommysql);
                redisUtil.hPutIfAbsent(REDIS_EMAIL_ID, findfrommysql.getEmail(), String.valueOf(findfrommysql.getId()));
                saveEmailIdToRedis(findfrommysql);
            }
        }
        if (findfrommysql == null) {
            //没有进入到查找主数据库
            String json = (String) redisUtil.hGet(REDIS_HASH_USER, String.valueOf(id));
            if (json == null) {
                //Redis中没有该键值对,查找主数据库
                findfrommysql = getFromMysqlBySelective(body, oldPwd);
                if (findfrommysql == null) {
                    return false;
                }
                saveUserToRedis(findfrommysql);
            }

            //Redis中取出的不为null
            u = JSONObject.parseObject(json, User.class);
            if (!oldPwd.equals(u.getPwd())) {
                //原密码错误
                return false;
            }
        } else {
            u = findfrommysql;
        }

        //进入了主数据库查询,说明密码正确，不用再次检验
        //修改用户密码
        u.setPwd(body.getPwd());
        redisUtil.hDelete(REDIS_HASH_USER, String.valueOf(u.getId()));
        int res = userMapper.updateByPrimaryKeySelective(u);
        if (res == 1) {
            return true;
        }
        return false;
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

    public User getFromMysqlBySelective(RegiestBody body, String oldpwd) {
        User u = new User();
        u.setEmail(body.getEmail());
        u.setName(body.getUsername());
        u.setPwd(oldpwd);
        //顺带密码验证
        return userMapper.selectSelective(u);
    }

}

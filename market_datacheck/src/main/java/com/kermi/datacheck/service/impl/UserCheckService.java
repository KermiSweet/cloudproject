package com.kermi.datacheck.service.impl;

import com.kermi.common.mapper.UserMapper;
import com.kermi.common.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.RedisUtil;

@Service
public class UserCheckService implements com.kermi.datacheck.service.UserCheckService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean emailExist(String email) {
        //先查询Redis有无此Email
        if (redisUtil.sIsMember("emailSet", email)){
            return true;
        }
        //redis中没有查询到，或者无此数据，查询主数据库
        User u = new User();u.setEmail(email);
        u = userMapper.selectSelective(u);
        if (u != null) {
            return true;
        }
        return false;
    }
}

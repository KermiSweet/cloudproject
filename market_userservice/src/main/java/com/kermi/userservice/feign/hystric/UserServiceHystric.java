package com.kermi.userservice.feign.hystric;

import com.alibaba.fastjson.JSONObject;
import com.kermi.common.mapper.UserMapper;
import com.kermi.common.pojo.User;
import com.kermi.common.requestBody.RegiestBody;
import com.kermi.userservice.feign.UserServiceFegin;
import entity.ResResult;
import entity.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class UserServiceHystric implements UserServiceFegin {

    private Logger logger = LoggerFactory.getLogger(UserServiceHystric.class);

    @Autowired
    private UserMapper userMapper;

    //TODO Userservice熔断机制
    @Override
    public ResResult login(RegiestBody body) {
        return new ResResult(false, StatusCode.UNKNOWSERVICEERROR, "登录失败");
    }

    @Override
    public ResResult regiest(RegiestBody body) {
        logger.warn("触发熔断器" + Thread.currentThread().getStackTrace()[1].getClassName() + new Exception().getStackTrace()[0].getMethodName());
        //TODO 如果用户已经插入mysql数据库，返回成功，没有则返回失败
        User user = new User();
        user.setName(body.getUsername());
        user.setEmail(body.getEmail());
        user.setPwd(body.getPwd());
        user = userMapper.selectSelective(user);
        if (user == null) {
            return new ResResult(false, StatusCode.UNKNOWSERVICEERROR, "注册失败");
        }
        user.setPwd("");
        String data = JSONObject.toJSONString(user);
        return new ResResult(true, StatusCode.OK, "注册成功", data);
    }

    @Override
    public ResResult sendCode(String email) {
        return new ResResult(false, StatusCode.UNKNOWSERVICEERROR, "发送失败");
    }

    @Override
    public ResResult forgetPassword(RegiestBody body) {
        return new ResResult(false, StatusCode.UNKNOWSERVICEERROR, "修改失败");
    }

    @Override
    public String sessionId() {
        return null;
    }
}

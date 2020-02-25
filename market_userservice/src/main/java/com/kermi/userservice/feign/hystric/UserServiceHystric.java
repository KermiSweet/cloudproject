package com.kermi.userservice.feign.hystric;

import com.kermi.common.requestBody.RegiestBody;
import com.kermi.userservice.feign.UserServiceFegin;
import entity.ResResult;
import org.springframework.stereotype.Component;

//@Component
public class UserServiceHystric implements UserServiceFegin {
    //TODO Userservice熔断机制
    @Override
    public ResResult login(RegiestBody body) {
        return null;
    }

    @Override
    public ResResult regiest(RegiestBody body) {
        //TODO 如果用户已经插入mysql数据库，返回成功，没有则返回失败
        return null;
    }

    @Override
    public ResResult sendCode(String email) {
        return null;
    }

    @Override
    public String sessionId() {
        return null;
    }
}

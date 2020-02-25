package com.kermi.userservice.controller;

import com.kermi.common.requestBody.RegiestBody;
import com.kermi.userservice.feign.DataCheckServiceFegin;
import com.kermi.userservice.feign.UserServiceFegin;
import entity.ResResult;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private DataCheckServiceFegin checkservice;

    @Autowired
    private UserServiceFegin userservice;

    @RequestMapping("/login")
    public ResResult login(@RequestBody RegiestBody body) {
        boolean flag = false;
        if (body.getUsername() != null && !"".equals(body.getUsername())) {
            flag = checkservice.nameExist(body.getUsername());
        }
        if (body.getEmail() != null && !"".equals(body.getEmail())) {
            flag = checkservice.emailExist(body.getEmail());
        }
        //flag为false的话两个都不存在，直接返回错误
        if (!flag) {
            return new ResResult(false, StatusCode.USERNAMEOREMAILNOTEXIST, "用户名或邮箱不存在");
        }
        return userservice.login(body);
    }

    @RequestMapping("/getsessionid")
    public String sessionId() {
        return userservice.sessionId();
    }
}

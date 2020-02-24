package com.kermi.userservice.controller;

import com.kermi.userservice.feign.DataCheckServiceFegin;
import com.kermi.userservice.feign.UserServiceFegin;
import entity.ResResult;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResResult login(@RequestParam(value = "email", required = false, defaultValue = "") String email,
                           @RequestParam(value = "username",required = false, defaultValue = "") String username,
                           @RequestParam(value = "pwd", required = true) String pwd){
        boolean flag=false;
        if (username != null && !"".equals(username)) {
            flag = checkservice.nameExist(username);
        }
        if (email != null && !"".equals(email)) {
            flag = checkservice.emailExist(email);
        }
        //flag为false的话两个都不存在，直接返回错误
        if (!flag){
            return new ResResult(false, StatusCode.USERNAMEOREMAILNOTEXIST, "用户名或邮箱不存在");
        }
        return userservice.login(email, username, pwd);
    }

    //TODO 服务连接失败
}

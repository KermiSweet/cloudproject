package com.kermi.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.kermi.base.service.UserService;
import com.kermi.common.pojo.User;
import com.kermi.common.requestBody.RegiestBody;
import entity.ResResult;
import entity.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public ResResult login(@RequestParam(value = "email", required = false, defaultValue = "") String email,
                           @RequestParam(value = "username",required = false, defaultValue = "") String username,
                           @RequestParam(value = "pwd", required = true) String pwd) {
        User user = userService.login(username, email, pwd);
        if (user == null) {
            //登录失败，用户名或密码不正确
            return new ResResult(true, StatusCode.LOGINGERROR, "用户名或密码不正确");
        }
        //密码不能对外公开,返回时设置为空值
        user.setPwd("");
        String msg = JSONObject.toJSONString(user);
        return new ResResult(true, StatusCode.OK, user.getName()+"登录成功", msg);
    }

    @RequestMapping("/regiest")
    public ResResult regiest(@RequestBody RegiestBody regiestuser) {
        // TODO 注册用户
        return null;
    }
}

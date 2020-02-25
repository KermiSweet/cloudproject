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

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping("/regiest")
    public ResResult regiest(@RequestBody RegiestBody body) {
        //先查看用户名和邮箱是否存在
        if (checkservice.nameExist(body.getUsername()) || checkservice.emailExist(body.getEmail())) {
            return new ResResult(false, StatusCode.ERROR, "用户名或邮箱已经存在");
        }
        //TODO feign结合 前端先自动进行一次请求，请求/user/resendCode 参数为email
        if (body.getCode() == null || "".equals(body.getCode())) {
            //验证码为空
            return new ResResult(false, StatusCode.ERROR, "缺少验证码");
        }
        //验证码不为空
        int res = checkservice.codeCheck(body.getCode());
        if (res == 2) {
            return new ResResult(false, StatusCode.ERROR, "验证码已过期");
        }
        if (res == 1) {
            return userservice.regiest(body);
        }
        return new ResResult(false, StatusCode.ERROR, "验证码错误");
    }

    @RequestMapping("/resendCode")
    public ResResult resendCode(@RequestParam("email") String email) {
        return userservice.sendCode(email);
    }

    @RequestMapping("/getSessionId")
    public String sessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }

    @RequestMapping("/getSessionId1")
    public String sessionId1(){
        return userservice.sessionId();
    }

    @RequestMapping("/getSessionId2")
    public String sessionId2(){
        return checkservice.sessionId();
    }

}

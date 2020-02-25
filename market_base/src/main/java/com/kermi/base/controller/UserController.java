package com.kermi.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.kermi.base.service.EmailService;
import com.kermi.base.service.SessionService;
import com.kermi.base.service.UserService;
import com.kermi.common.pojo.User;
import com.kermi.common.requestBody.RegiestBody;
import entity.ResResult;
import entity.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.SessionAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResResult login(@RequestBody RegiestBody body,
                           HttpServletRequest request) {
        User user = userService.login(body.getUsername(), body.getEmail(), body.getPwd());
        if (user == null) {
            //登录失败，用户名或密码不正确
            return new ResResult(true, StatusCode.LOGINGERROR, "用户名或密码不正确");
        }
        //密码不能对外公开,返回时设置为空值
        user.setPwd("");
        String msg = JSONObject.toJSONString(user);
        //登录成功Session存储
        SessionAttributes attributes = new SessionAttributes(getSessionId(request), user);
        sessionService.writeSessionAttributesToRedis(attributes);
        return new ResResult(true, StatusCode.OK, user.getName() + "登录成功", msg);
    }

    @RequestMapping(value = "/regiest", method = RequestMethod.POST)
    public ResResult regiest(@RequestBody RegiestBody regiestuser, HttpServletRequest request) {
        //进行邮箱验证
        //发送要发送的内容到消息队列中
        //保存当前的randomcode到缓存服务器用来等待验证
        emailService.saveCodeToRedis(getSessionId(request), emailService.sendEmailToRbq(regiestuser.getEmail()));
        //TODO 在执行此控制器之前，先经过datacheck验证
        //注册用户
        User regiest = userService.regiest(regiestuser);
        if (regiest == null) {
            return new ResResult(true, StatusCode.ERROR, "注册失败");
        }
        String message = regiest.getEmail() + "(" + regiest.getName() + ")" + "注册成功";
        regiest.setPwd("");
        String data = JSONObject.toJSONString(regiest);
        return new ResResult(true, StatusCode.OK, message, data);
    }

    //test
    @RequestMapping("/getSessionId")
    public String sessionId(HttpServletRequest request) {
        return getSessionId(request);
    }

    @RequestMapping("/resendCode")
    public ResResult sendCode(@RequestParam String email, HttpServletRequest request) {
        emailService.saveCodeToRedis(getSessionId(request), emailService.sendEmailToRbq(email));
        return new ResResult(true, StatusCode.OK, "发送成功");
    }
    //test

    public String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }
}

package com.kermi.base.controller;

import com.alibaba.fastjson.JSONObject;
import com.kermi.base.feign.DataCheckFegin;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private DataCheckFegin checkFegin;

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResResult login(@RequestBody RegiestBody body,
                           HttpServletRequest request) {
        boolean flag = false;
        if (body.getUsername() != null && !"".equals(body.getUsername())) {
            flag = checkFegin.nameExist(body.getUsername());
        }
        if (body.getEmail() != null && !"".equals(body.getEmail())) {
            flag = checkFegin.emailExist(body.getEmail());
        }
        //flag为false的话两个都不存在，直接返回错误
        if (!flag) {
            return new ResResult(false, StatusCode.USERNAMEOREMAILNOTEXIST, "用户名或邮箱不存在");
        }

        return loginMain(body, request);
    }

    @RequestMapping(value = "/regiest", method = RequestMethod.POST)
    public ResResult regiest(@RequestBody RegiestBody regiestuser) {

        //先查看用户名和邮箱是否存在
        if (checkFegin.nameExist(regiestuser.getUsername()) || checkFegin.emailExist(regiestuser.getEmail())) {
            return new ResResult(false, StatusCode.ERROR, "用户名或邮箱已经存在");
        }
        //feign结合 前端先自动进行一次请求，请求/user/resendCode 参数为email
        if (regiestuser.getCode() == null || "".equals(regiestuser.getCode())) {
            //验证码为空
            return new ResResult(false, StatusCode.ERROR, "缺少验证码");
        }
        //验证码不为空
        int res = checkFegin.codeCheck(regiestuser.getCode());
        if (res == 2) {
            return new ResResult(false, StatusCode.ERROR, "验证码已过期");
        }
        if (res == 1) {
            return regiestMain(regiestuser);
        }
        return new ResResult(false, StatusCode.ERROR, "验证码错误");

    }

    @RequestMapping(value = "/sendCode", method = RequestMethod.GET)
    public ResResult sendCode(@RequestParam String email, HttpServletRequest request) {
        emailService.saveCodeToRedis(getSessionId(request), emailService.sendEmailToRbq(email));
        return new ResResult(true, StatusCode.OK, "发送成功");
    }

    @RequestMapping(value = "/forgetpassword", method = RequestMethod.POST)
    public ResResult forgetpassword(@RequestBody RegiestBody body) {
        // 忘记密码测试，编写重置密码
        //先检查目标邮箱是否存在
        if (!checkFegin.emailExist(body.getEmail())) {
            return new ResResult(false, StatusCode.ERROR, "邮箱不存在,请先注册");
        }
        //feign结合 前端先自动进行一次请求，请求/user/resendCode 参数为email
        if (body.getCode() == null || "".equals(body.getCode())) {
            //验证码为空
            return new ResResult(false, StatusCode.ERROR, "缺少验证码");
        }
        int res = checkFegin.codeCheck(body.getCode());
        if (res == 2) {
            return new ResResult(false, StatusCode.ERROR, "验证码已过期");
        }
        if (res == 1) {
            boolean flag = userService.resetPassword(body);
            if (flag) {
                return new ResResult(true, StatusCode.OK, "密码修改成功");
            }
            return new ResResult(false, StatusCode.ERROR, "密码修改失败");
        }

        return new ResResult(false, StatusCode.ERROR, "验证码错误");
    }

    @RequestMapping(value = "/changepassword", method = RequestMethod.POST)
    public ResResult changepassword(@RequestBody RegiestBody body, @RequestParam("oldpwd") String oldPwd) {
        if (body.getEmail() != null) {
            if ("".equals(body.getEmail())) {
                return new ResResult(false, StatusCode.PARAMSLOST, "邮箱不能为空");
            }
            if (!checkFegin.emailExist(body.getEmail())) {
                return new ResResult(false, StatusCode.USERNAMEOREMAILNOTEXIST, "邮箱不存在");
            }
        }
        if (body.getUsername() != null) {
            if ("".equals(body.getUsername())) {
                return new ResResult(false, StatusCode.PARAMSLOST, "用户名不能为空");
            }
            if (!checkFegin.nameExist(body.getUsername())) {
                return new ResResult(false, StatusCode.USERNAMEOREMAILNOTEXIST, "用户名不存在");
            }
        }


        boolean res = userService.changePwd(body, oldPwd);
        if (res) {
            return new ResResult(true, StatusCode.OK, "密码修改成功");
        }
        return new ResResult(false, StatusCode.ERROR, "密码修改失败");
    }


    @RequestMapping("/logout")
    public ResResult logout(HttpServletRequest request) {
        userService.logout(getSessionId(request));
        return new ResResult(true, StatusCode.OK, "登出成功");
    }

    //test
    @RequestMapping("/getSessionId")
    public String sessionId(HttpServletRequest request) {
        return getSessionId(request);
    }
    //test

    public String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }

    public ResResult loginMain(RegiestBody body, HttpServletRequest request) {
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

    public ResResult regiestMain(RegiestBody regiestuser) {
        //进行邮箱验证
        //发送要发送的内容到消息队列中
        //保存当前的randomcode到缓存服务器用来等待验证
        //在执行此控制器之前，先经过datacheck验证
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
}

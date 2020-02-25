package com.kermi.datacheck.controller;

import com.kermi.datacheck.service.UserCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user/check")
@Slf4j
public class UserCheckController {

    @Autowired
    private UserCheckService checkService;

    @RequestMapping(value = "/mail", method = RequestMethod.GET)
    public boolean emailExist(@RequestParam("email") String email) {
        return checkService.emailExist(email);
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    public boolean nameExist(@RequestParam("username") String username) {
        return checkService.nameExist(username);
    }

    @RequestMapping(value = "/codeCheck", method = RequestMethod.GET)
    public int codeCheck(@RequestParam("code") String code, HttpServletRequest request) {
        return checkService.codeCheck(getSessionId(request), code);
    }

    //test
    @RequestMapping(value = "/getSessionId")
    public String sessionid(HttpServletRequest request) {
        return getSessionId(request);
    }

    public String getSessionId(HttpServletRequest request) {
        return request.getSession().getId();
    }

}

package com.kermi.datacheck.controller;

import com.kermi.datacheck.service.UserCheckService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}

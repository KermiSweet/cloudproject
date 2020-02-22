package com.kermi.datacheck.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/check")
@Api(value = "UserCheckController", description = "用户数据检索")
@Slf4j
public class UserCheckController {

    private Logger logger = LoggerFactory.getLogger(UserCheckController.class);

    public boolean emailExist(@RequestParam("email") String email) {
        // TODO userservice.emailExist(email)
        return false;
    }

}

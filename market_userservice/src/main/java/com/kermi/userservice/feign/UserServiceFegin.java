package com.kermi.userservice.feign;

import entity.ResResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "market-base")
public interface UserServiceFegin {

    @RequestMapping(value = "/api/user/login", method = RequestMethod.GET)
    ResResult login(@RequestParam(value = "email", required = false, defaultValue = "") String email,
                    @RequestParam(value = "username",required = false, defaultValue = "") String username,
                    @RequestParam(value = "pwd", required = true) String pwd);
}

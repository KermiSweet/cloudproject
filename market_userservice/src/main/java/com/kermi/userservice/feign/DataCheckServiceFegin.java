package com.kermi.userservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "datacheck-service")
public interface DataCheckServiceFegin {

    @RequestMapping(value = "/api/user/check/mail", method = RequestMethod.GET)
    boolean emailExist(@RequestParam("email") String email);

    @RequestMapping(value = "/api/user/check/username", method = RequestMethod.GET)
    boolean nameExist(@RequestParam("username") String username);
}

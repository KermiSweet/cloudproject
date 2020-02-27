package com.kermi.base.feign;


import com.kermi.base.feign.hystrix.DataCheckFeignHystirx;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "datacheck-service", fallback = DataCheckFeignHystirx.class)
public interface DataCheckFegin {

    @RequestMapping(value = "/api/user/check/mail", method = RequestMethod.GET)
    boolean emailExist(@RequestParam("email") String email);

    @RequestMapping(value = "/api/user/check/username", method = RequestMethod.GET)
    boolean nameExist(@RequestParam("username") String username);

    @RequestMapping(value = "/api/user/check/codeCheck", method = RequestMethod.GET)
    int codeCheck(@RequestParam("code") String code);

    @RequestMapping(value = "/api/user/check/getSessionId")
    String sessionId();
}


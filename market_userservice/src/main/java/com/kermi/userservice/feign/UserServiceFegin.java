package com.kermi.userservice.feign;

import com.kermi.common.requestBody.RegiestBody;
import entity.ResResult;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "market-base")
public interface UserServiceFegin {

    @RequestMapping(value = "/api/user/login", method = RequestMethod.POST)
    ResResult login(@RequestBody RegiestBody body);

    @RequestMapping(value = "/api/user/regiest", method = RequestMethod.POST)
    ResResult regiest(@RequestBody RegiestBody body);

    @RequestMapping(value = "/api/user/sendCode", method = RequestMethod.GET)
    ResResult sendCode(@RequestParam("email") String email);

    @RequestMapping(value = "/api/user/getSessionId")
    String sessionId();

}

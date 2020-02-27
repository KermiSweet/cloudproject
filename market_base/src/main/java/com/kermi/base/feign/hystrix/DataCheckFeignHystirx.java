package com.kermi.base.feign.hystrix;

import com.kermi.base.feign.DataCheckFegin;
import org.springframework.stereotype.Component;

@Component
public class DataCheckFeignHystirx implements DataCheckFegin {
    @Override
    public boolean emailExist(String email) {
        return false;
    }

    @Override
    public boolean nameExist(String username) {
        return false;
    }

    @Override
    public int codeCheck(String code) {
        return 0;
    }

    @Override
    public String sessionId() {
        return null;
    }
}

package com.kermi.base.service;


import com.kermi.common.pojo.User;
import com.kermi.common.requestBody.RegiestBody;

public interface UserService {

    /**
     * 用户登录
     * @param username
     * @param email
     * @param pwd
     * @return loginUser or null
     */
    User login(String username, String email, String pwd);

    /**
     * 用户注册
     * @param regiestBody
     * @return User or null
     */
    User regiest(RegiestBody regiestBody);


}

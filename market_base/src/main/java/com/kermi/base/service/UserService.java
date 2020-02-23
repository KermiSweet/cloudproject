package com.kermi.base.service;


import com.kermi.common.pojo.User;

public interface UserService {

    /**
     * 用户登录
     * @param username
     * @param email
     * @param pwd
     * @return loginUser or null
     */
    User login(String username, String email, String pwd);
}

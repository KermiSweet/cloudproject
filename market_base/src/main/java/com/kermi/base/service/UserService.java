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


    /**
     * 忘记密码重设密码
     * @param regiestBody
     * @return true or false
     */
    boolean resetPassword(RegiestBody regiestBody);

    /**
     * 修改密码
     * @param body
     * @param oldPwd
     * @return true or false
     */
    boolean changePwd(RegiestBody body, String oldPwd);

    /**
     * 退出登录
     * @param sessionId
     */
    void logout(String sessionId);
}

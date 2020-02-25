package com.kermi.datacheck.service;

/**
 * 部分不能重复数据检索重复性
 */
public interface UserCheckService {

    /**
     * 邮箱是否存在
     * @param email
     * @return true or false
     */
    public boolean emailExist(String email);

    /**
     * 用户名是否存在
     * @param username
     * @return true or false
     */
    public boolean nameExist(String username);

    /**
     * 验证邮箱验证码是否正确
     * @param sessionId
     * @param code
     * @return 0(错误),1(正确),3(超时)
     */
    int codeCheck(String sessionId, String code);
}

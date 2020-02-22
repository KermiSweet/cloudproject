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
}

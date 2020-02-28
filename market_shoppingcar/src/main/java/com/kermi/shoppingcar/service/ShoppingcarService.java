package com.kermi.shoppingcar.service;

public interface ShoppingcarService {
    /**
     * 添加商品进购物车
     *
     * @param id
     * @param nums
     * @param sessionid
     * @param code 1为添加 0为删除
     * @return true or false
     */
    boolean caroperation(Long id, int nums, String sessionid, int code);
}

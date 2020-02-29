package com.kermi.shoppingcar.service;

import com.github.pagehelper.PageInfo;
import com.kermi.shoppingcar.utils.ShoppingcarGetPrice;
import com.kermi.shoppingcar.utils.ShoppingcarRedisItemUtil;

import java.util.List;

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

    /**
     * 用mybatis
     * @param sessionId
     * @return List<ShoppincarRedisItemUtil>
     */
    List<ShoppingcarRedisItemUtil> getcars(String sessionId);

    /**
     * 清空购物车
     * @param sessionId
     * @return
     */
    boolean clearall(String sessionId);

    /**
     * 得到选中的商品的价格，提交订单时触发
     * @param goodslist
     * @return
     */
    double getprice(ShoppingcarGetPrice goodslist);
}

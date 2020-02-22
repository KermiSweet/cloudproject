package com.kermi.common.mapper;

import com.kermi.common.pojo.ShoppingCar;

public interface ShoppingCarMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShoppingCar record);

    int insertSelective(ShoppingCar record);

    ShoppingCar selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShoppingCar record);

    int updateByPrimaryKey(ShoppingCar record);
}
package com.kermi.common.mapper;

import com.kermi.common.pojo.ShoppingCar;

import java.util.List;

public interface ShoppingCarMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShoppingCar record);

    int insertSelective(ShoppingCar record);

    ShoppingCar selectByPrimaryKey(Long id);

    List<ShoppingCar> selectByUserId(Long id);

    int updateByPrimaryKeySelective(ShoppingCar record);

    int updateByPrimaryKey(ShoppingCar record);
}
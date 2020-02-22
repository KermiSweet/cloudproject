package com.generate.dao;

import com.generate.pojo.OrderBase;

public interface OrderBaseMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderBase record);

    int insertSelective(OrderBase record);

    OrderBase selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderBase record);

    int updateByPrimaryKey(OrderBase record);
}
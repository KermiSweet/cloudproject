package com.generate.dao;

import com.generate.pojo.Business;

public interface BusinessMapper {
    int insert(Business record);

    int insertSelective(Business record);
}
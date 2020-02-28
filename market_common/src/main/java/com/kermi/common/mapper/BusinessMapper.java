package com.kermi.common.mapper;

import com.kermi.common.pojo.Business;

public interface BusinessMapper {
    int insert(Business record);

    int insertSelective(Business record);
}
package com.generate.dao;

import com.generate.pojo.Admin;

public interface AdminMapper {
    int insert(Admin record);

    int insertSelective(Admin record);
}
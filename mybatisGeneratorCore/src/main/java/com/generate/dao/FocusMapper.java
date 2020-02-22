package com.generate.dao;

import com.generate.pojo.Focus;

public interface FocusMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Focus record);

    int insertSelective(Focus record);

    Focus selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Focus record);

    int updateByPrimaryKey(Focus record);
}
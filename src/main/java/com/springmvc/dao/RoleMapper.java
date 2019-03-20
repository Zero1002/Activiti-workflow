package com.springmvc.dao;

import com.springmvc.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    Long selectCnt(@Param(value = "params") Map<String, Object> params);

    List<Role> list(@Param(value = "params") Map<String, Object> params);
}
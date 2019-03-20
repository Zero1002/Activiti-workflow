package com.springmvc.service;

import com.springmvc.pojo.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    Long selectCnt(Map<String, Object> params);

    List<Role> list(Map<String, Object> params);
}

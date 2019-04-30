package com.springmvc.service;

import com.springmvc.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    Long selectCnt(Map<String, Object> params);

    List<User> list( Map<String, Object> params);

    List<User> listByRoleId(Integer roleId);

    User findUserByNamePwd(User record);
}

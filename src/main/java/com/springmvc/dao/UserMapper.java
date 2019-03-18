package com.springmvc.dao;

import com.springmvc.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    Long selectCnt(@Param(value = "params") Map<String, Object> params);

    List<User> list(@Param(value = "params") Map<String, Object> params);

    User findUserByNamePwd(User record);
}
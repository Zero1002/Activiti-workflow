package com.springmvc.service.impl;

import com.springmvc.dao.UserMapper;
import com.springmvc.pojo.User;
import com.springmvc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("UserService")
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    public int deleteByPrimaryKey(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    public int insert(User record) {
        return userMapper.insert(record);
    }

    public int insertSelective(User record) {
        return userMapper.insertSelective(record);
    }

    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(User record) {
        return userMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(User record) {
        return userMapper.updateByPrimaryKey(record);
    }

    public Long selectCnt(Map<String, Object> params) {
        return userMapper.selectCnt(params);
    }

    public List<User> list(Map<String, Object> params) {
        return userMapper.list(params);
    }

    public List<User> listByRoleId(Integer roleId) {
        return userMapper.listByRoleId(roleId);
    }

    public User findUserByNamePwd(User record) {
        return userMapper.findUserByNamePwd(record);
    }


}

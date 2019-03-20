package com.springmvc.service.impl;

import com.springmvc.dao.RoleMapper;
import com.springmvc.pojo.Role;
import com.springmvc.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("RoleService")
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    public int deleteByPrimaryKey(Integer id) {
        return roleMapper.deleteByPrimaryKey(id);
    }

    public int insert(Role record) {
        return roleMapper.insert(record);
    }

    public int insertSelective(Role record) {
        return roleMapper.insertSelective(record);
    }

    public Role selectByPrimaryKey(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(Role record) {
        return roleMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(Role record) {
        return roleMapper.updateByPrimaryKey(record);
    }

    public Long selectCnt(Map<String, Object> params) {
        return roleMapper.selectCnt(params);
    }

    public List<Role> list(Map<String, Object> params) {
        return roleMapper.list(params);
    }
}

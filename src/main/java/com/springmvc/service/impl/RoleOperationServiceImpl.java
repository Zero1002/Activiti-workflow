package com.springmvc.service.impl;

import com.springmvc.dao.RoleOperationMapper;
import com.springmvc.pojo.RoleOperation;
import com.springmvc.service.RoleOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("RoleOperationService")
public class RoleOperationServiceImpl implements RoleOperationService {
    @Autowired
    private RoleOperationMapper roleOperationMapper;

    public int deleteByPrimaryKey(Integer id) {
        return roleOperationMapper.deleteByPrimaryKey(id);
    }

    public int insert(RoleOperation record) {
        return roleOperationMapper.insert(record);
    }

    public int insertSelective(RoleOperation record) {
        return roleOperationMapper.insertSelective(record);
    }

    public RoleOperation selectByPrimaryKey(Integer id) {
        return roleOperationMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(RoleOperation record) {
        return roleOperationMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(RoleOperation record) {
        return roleOperationMapper.updateByPrimaryKey(record);
    }

    public List<RoleOperation> find(Map<String, Object> map) {
        return roleOperationMapper.find(map);
    }

    public Long getTotal(Map<String, Object> map) {
        return roleOperationMapper.getTotal(map);
    }
}

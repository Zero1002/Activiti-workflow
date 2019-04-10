package com.springmvc.dao;

import com.springmvc.pojo.RoleOperation;

import java.util.List;
import java.util.Map;

public interface RoleOperationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleOperation record);

    int insertSelective(RoleOperation record);

    RoleOperation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleOperation record);

    int updateByPrimaryKey(RoleOperation record);

    /**
     * 根据条件查询申请单集合
     */
    List<RoleOperation> find(Map<String, Object> map);

    /**
     * 获取总记录数
     * @param map
     * @return
     */
    Long getTotal(Map<String, Object> map);
}
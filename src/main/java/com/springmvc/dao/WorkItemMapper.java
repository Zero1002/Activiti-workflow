package com.springmvc.dao;

import com.springmvc.pojo.WorkItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface WorkItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkItem record);

    int insertSelective(WorkItem record);

    WorkItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkItem record);

    int updateByPrimaryKeyWithBLOBs(WorkItem record);

    int updateByPrimaryKey(WorkItem record);

    List<WorkItem> list(@Param(value = "params") Map<String, Object> params);
}
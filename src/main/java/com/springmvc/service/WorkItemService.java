package com.springmvc.service;

import com.springmvc.pojo.WorkItem;

import java.util.List;
import java.util.Map;

public interface WorkItemService {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkItem record);

    int insertSelective(WorkItem record);

    WorkItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkItem record);

    int updateByPrimaryKeyWithBLOBs(WorkItem record);

    int updateByPrimaryKey(WorkItem record);

    List<WorkItem> list(Map<String, Object> params);
}

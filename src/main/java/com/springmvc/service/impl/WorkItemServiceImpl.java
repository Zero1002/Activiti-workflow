package com.springmvc.service.impl;

import com.springmvc.dao.WorkItemMapper;
import com.springmvc.pojo.WorkItem;
import com.springmvc.service.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("WorkItemService")
public class WorkItemServiceImpl implements WorkItemService {
    @Autowired
    private WorkItemMapper workItemMapper;

    public int deleteByPrimaryKey(Integer id) {
        return workItemMapper.deleteByPrimaryKey(id);
    }

    public int insert(WorkItem record) {
        return workItemMapper.insert(record);
    }

    public int insertSelective(WorkItem record) {
        return workItemMapper.insertSelective(record);
    }

    public WorkItem selectByPrimaryKey(Integer id) {
        return workItemMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(WorkItem record) {
        return workItemMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKeyWithBLOBs(WorkItem record) {
        return workItemMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    public int updateByPrimaryKey(WorkItem record) {
        return workItemMapper.updateByPrimaryKey(record);
    }

    public List<WorkItem> list(Map<String, Object> params) {
        return workItemMapper.list(params);
    }
}

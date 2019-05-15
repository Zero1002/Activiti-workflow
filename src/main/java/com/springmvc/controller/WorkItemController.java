package com.springmvc.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springmvc.pojo.User;
import com.springmvc.pojo.WorkItem;
import com.springmvc.service.UserService;
import com.springmvc.service.WorkItemService;
import com.springmvc.utils.ResponseObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workItem")
public class WorkItemController {
    @Autowired
    private WorkItemService workItemService;
    @Autowired
    private UserService userService;

    // 获取列表
    @ResponseBody
    @RequestMapping("/list")
    public ResponseObject<Map<String, Object>> list(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> aRetMap = new HashMap<String, Object>();
        List<WorkItem> list = workItemService.list(params);
        aRetMap.put("list", list);
        for (WorkItem item : list) {
            User user = userService.selectByPrimaryKey(item.getAdminId());
            item.setAdminName(user.getLoginName());
        }
        return new ResponseObject<Map<String, Object>>(aRetMap);
    }

    // 增改
    @ResponseBody
    @RequestMapping(value = "/save")
    public ModelAndView save(WorkItem record) {
        int rc = 0;
        if (record.getId() != null) {
            rc = workItemService.updateByPrimaryKeySelective(record);
        } else {
            rc = workItemService.insertSelective(record);
        }
        String msgReturn = "";
        if (rc > 0) {
            msgReturn = "保存成功";
        } else {
            msgReturn = "保存失败";
        }
        Map<String, Object> params = new HashMap<String, Object>();
        List<WorkItem> list = workItemService.list(params);
        for (WorkItem item : list) {
            User user = userService.selectByPrimaryKey(item.getAdminId());
            item.setAdminName(user.getLoginName());
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("msgReturn", msgReturn);
        mv.addObject("workItemList", list);
        mv.setViewName("views/workItemManagement");
        return mv;
    }

    // 删除
    @ResponseBody
    @RequestMapping("/delete")
    public ResponseObject<Boolean> delete(Integer id) {
        WorkItem record = workItemService.selectByPrimaryKey(id);
        record.setIsDel(true);
        int rc = workItemService.updateByPrimaryKey(record);
        return new ResponseObject<Boolean>(rc > 0);
    }

    // 查询某一个
    @ResponseBody
    @RequestMapping("/{id}")
    public ModelAndView view(@PathVariable Integer id, HttpServletRequest req) {
        WorkItem record = workItemService.selectByPrimaryKey(id);
        ModelAndView mv = new ModelAndView();
        if (record.getExtraInfo() != null) {
            try {
                // 读取blob格式并转化成json对象
                String extraInfo = new String(record.getExtraInfo(), "GB2312");
                JSONObject extraInfoJson = JSONObject.fromObject(extraInfo);
                mv.addObject("extraInfoJson", extraInfoJson);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        mv.addObject("workItem", record);
        mv.setViewName("views/workItemEdit");
        return mv;
    }
}

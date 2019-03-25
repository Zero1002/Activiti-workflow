package com.springmvc.controller;

import com.springmvc.pojo.Role;
import com.springmvc.service.RoleService;
import com.springmvc.utils.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    // 获取列表
    @ResponseBody
    @RequestMapping("/list")
    public ResponseObject<Map<String, Object>> list(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> aRetMap = new HashMap<String, Object>();
        aRetMap.put("total", roleService.selectCnt(params));
        aRetMap.put("list", roleService.list(params));
        return new ResponseObject<Map<String, Object>>(aRetMap);
    }

    // 增改
    @ResponseBody
    @RequestMapping(value = "/save")
    public ModelAndView save(Role record) {
        int rc = 0;
        if (record.getId() != null) {
            rc = roleService.updateByPrimaryKeySelective(record);
        } else {
            rc = roleService.insertSelective(record);
        }
        String msgReturn = "";
        if (rc > 0) {
            msgReturn = "保存成功";
        } else {
            msgReturn = "保存失败";
        }
        Map<String, Object> params = new HashMap<String, Object>();
        List<Role> rolelists = roleService.list(params);
        ModelAndView mv = new ModelAndView();
        mv.addObject("msgReturn", msgReturn);
        mv.addObject("rolelists", rolelists);
        mv.setViewName("views/roleManagement");
        return mv;
    }

    // 删除
    @ResponseBody
    @RequestMapping("/delete")
    public ResponseObject<Boolean> delete(Integer id) {
        Role record = roleService.selectByPrimaryKey(id);
        record.setIsDel(true);
        int rc = roleService.updateByPrimaryKeySelective(record);
        return new ResponseObject<Boolean>(rc > 0);
    }

    // 查询某一个
    @ResponseBody
    @RequestMapping("/{id}")
    public ModelAndView view(@PathVariable Integer id, HttpServletRequest req) {
        Role record = roleService.selectByPrimaryKey(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("role", record);
        mv.setViewName("views/roleEdit");
        return mv;
    }
}

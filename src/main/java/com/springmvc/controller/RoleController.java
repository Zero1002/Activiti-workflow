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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
    public ResponseObject<Boolean> save(@RequestBody Role record) {
        int rc = 0;
        if (record.getId() != null) {
            rc = roleService.updateByPrimaryKeySelective(record);
        } else {
            rc = roleService.insert(record);
        }
        return new ResponseObject<Boolean>(rc > 0);
    }

    // 删除
    @ResponseBody
    @RequestMapping("/delete/{id}")
    public ResponseObject<Boolean> delete(@PathVariable Integer id) {
        int result = roleService.deleteByPrimaryKey(id);
        return new ResponseObject<Boolean>(result > 0);
    }

    // 查询某一个
    @ResponseBody
    @RequestMapping("/{id}")
    public ResponseObject<Role> view(@PathVariable Integer id, HttpServletRequest req) {
        Role record = roleService.selectByPrimaryKey(id);
        return new ResponseObject<Role>(record);
    }
}

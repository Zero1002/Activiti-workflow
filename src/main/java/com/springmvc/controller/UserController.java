package com.springmvc.controller;

import com.springmvc.pojo.User;
import com.springmvc.service.UserService;
import com.springmvc.utils.ResponseObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    // 获取列表
    @ResponseBody
    @RequestMapping("/list")
    public ResponseObject<Map<String, Object>> list(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> aRetMap = new HashMap<String, Object>();
        aRetMap.put("total", userService.selectCnt(params));
        aRetMap.put("list", userService.list(params));
        return new ResponseObject<Map<String, Object>>(aRetMap);
    }

    // 增改
    @ResponseBody
    @RequestMapping(value = "/save")
    public ResponseObject<Boolean> save(@RequestBody User record) {
        int rc = 0;
        if (record.getId() != null) {
            rc = userService.updateByPrimaryKeySelective(record);
        } else {
            rc = userService.insert(record);
        }
        return new ResponseObject<Boolean>(rc > 0);
    }

    // 删除
    @ResponseBody
    @RequestMapping("/delete/{id}")
    public ResponseObject<Boolean> delete(@PathVariable Integer id) {
        int result = userService.deleteByPrimaryKey(id);
        return new ResponseObject<Boolean>(result > 0);
    }

    // 查询某一个
    @ResponseBody
    @RequestMapping("/{id}")
    public ResponseObject<User> view(@PathVariable Integer id, HttpServletRequest req) {
        User user = userService.selectByPrimaryKey(id);
        return new ResponseObject<User>(user);
    }

    // 登陆
    @ResponseBody
    @RequestMapping("/login")
    public ModelAndView login(String loginName, String password) {
        User record = new User();
        record.setLoginName(loginName);
        record.setPassword(password);
        User user = userService.findUserByNamePwd(record);
        ModelAndView modelAndView = new ModelAndView();
        if(user!=null){
            modelAndView.addObject("user", user);
            modelAndView.setViewName("views/index");
        }else{
            modelAndView.addObject("message", "用户名或密码错误！");
            modelAndView.setViewName("views/login");
        }

        return modelAndView;
    }
}

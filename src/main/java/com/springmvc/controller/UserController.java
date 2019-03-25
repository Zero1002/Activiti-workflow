package com.springmvc.controller;

import com.springmvc.pojo.User;
import com.springmvc.service.UserService;
import com.springmvc.utils.ResponseObject;
import com.sun.deploy.net.HttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
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
    public ModelAndView save(User record, HttpServletRequest req) {
        int rc = 0;
        if (record.getId() != null) {
            rc = userService.updateByPrimaryKeySelective(record);
        } else {
            rc = userService.insertSelective(record);
        }
        String msgReturn = "";
        if (rc > 0) {
            msgReturn = "保存成功";
        } else {
            msgReturn = "保存失败";
        }
        Map<String, Object> params = new HashMap<String, Object>();
        List<User> userlists = userService.list(params);
        ModelAndView mv = new ModelAndView();
        mv.addObject("msgReturn", msgReturn);
        mv.addObject("userlists",userlists);
        mv.setViewName("views/userManagement");
        return mv;
    }

    // 删除
    @ResponseBody
    @RequestMapping("/delete")
    public ResponseObject<Boolean> delete(Integer id) {
        User user = userService.selectByPrimaryKey(id);
        user.setIsDel(true);
        int rc = userService.updateByPrimaryKeySelective(user);
        return new ResponseObject<Boolean>(rc > 0);
    }

    // 查询某一个
    @ResponseBody
    @RequestMapping("/{id}")
    public ModelAndView view(@PathVariable Integer id, HttpServletRequest req) {
        User user = userService.selectByPrimaryKey(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("user", user);
        mv.setViewName("views/userEdit");
        return mv;
    }

    // 登陆
    @ResponseBody
    @RequestMapping("/login")
    public ModelAndView login(String loginName, String password, HttpServletRequest request, HttpServletResponse response) {
        User record = new User();
        record.setLoginName(loginName);
        record.setPassword(password);
        User user = userService.findUserByNamePwd(record);
        ModelAndView modelAndView = new ModelAndView();
        if (user != null) {
            HttpSession session = request.getSession(true);
            // cookie
            Cookie userCookie = new Cookie("COOKIE_NAME", loginName);
            userCookie.setMaxAge(1 * 24 * 60 * 60); // 存活期为一天 1*24*60*60
            userCookie.setPath("/");
            response.addCookie(userCookie);
            // session
            session.setAttribute("SESSION_NAME", user.getLoginName());
            session.setAttribute("SESSION_ROLE_NAME", user.getRoleName());
            modelAndView.setViewName("views/index");
        } else {
            modelAndView.addObject("message", "用户名或密码错误！");
            modelAndView.setViewName("views/login");
        }

        return modelAndView;
    }

    // 退出登录
    @ResponseBody
    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession(true);
        session.removeAttribute("userSession");
        modelAndView.setViewName("views/login");
        return modelAndView;
    }
}

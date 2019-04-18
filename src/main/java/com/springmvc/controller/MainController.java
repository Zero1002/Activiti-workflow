package com.springmvc.controller;

import com.springmvc.pojo.Role;
import com.springmvc.pojo.User;
import com.springmvc.pojo.WorkItem;
import com.springmvc.service.RoleService;
import com.springmvc.service.UserService;
import com.springmvc.service.WorkItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private WorkItemService workItemService;

    /**
     * 首页登陆
     *
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("views/login");
        return modelAndView;
    }

    /**
     * 左侧栏跳转
     *
     * @return
     */
    @RequestMapping("/views")
    public ModelAndView views(String pageName) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("views/" + pageName);
        Map<String, Object> params = new HashMap<String, Object>();
        // 用户管理
        if (pageName.equals("userManagement")) {
            List<User> userlists = userService.list(params);
            modelAndView.addObject("userlists",userlists);
        }
        // 角色管理
        if (pageName.equals("roleManagement")) {
            List<Role> rolelists = roleService.list(params);
            modelAndView.addObject("rolelists",rolelists);
        }
        // 任务管理
        if (pageName.equals("workItemManagement")) {
            List<WorkItem> workItemList = workItemService.list(params);
            for (WorkItem item : workItemList) {
                User user = userService.selectByPrimaryKey(item.getAdminId());
                item.setAdminName(user.getLoginName());
            }
            modelAndView.addObject("workItemList",workItemList);
        }
        // 待办
        if (pageName.equals("toDoList")) {
            List<WorkItem> workItemList = workItemService.list(params);
            modelAndView.addObject("workItemList",workItemList);
        }
        return modelAndView;
    }
}

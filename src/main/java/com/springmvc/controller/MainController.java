package com.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

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
        return modelAndView;
    }
}

package com.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class main {
    @RequestMapping("/")
    @ResponseBody
    public String index(){
        return "hello world";
    }
}

package com.ty.controller;

import com.ty.pojo.ResponseData;
import com.ty.pojo.User;
import com.ty.util.ResponseUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {


    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseData login(User user) throws Exception{
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());


            subject.login(token);
            subject.isAuthenticated();
            return ResponseUtil.success("登录成功");


    }

    @RequestMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String index(Model model) {
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("username", username);
        return "index";
    }
}

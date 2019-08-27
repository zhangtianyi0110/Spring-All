package com.ty.springbootshiro.controller;

import com.ty.springbootshiro.pojo.ResponseData;
import com.ty.springbootshiro.pojo.User;
import com.ty.springbootshiro.util.ResponseUtil;
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
    public ResponseData login(User user){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());

        try {
            subject.login(token);
            return ResponseUtil.success("登录成功");
        }catch (UnknownAccountException e) {
            return ResponseUtil.failure(401,e.getMessage());
        } catch (IncorrectCredentialsException e) {
            return ResponseUtil.failure(401,e.getMessage());
        } catch (LockedAccountException e) {
            return ResponseUtil.failure(401,e.getMessage());
        } catch (AuthenticationException e) {
            return ResponseUtil.failure(401,"认证失败！");
        }

    }

    @RequestMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String index(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("user", user);
        return "index";
    }
}

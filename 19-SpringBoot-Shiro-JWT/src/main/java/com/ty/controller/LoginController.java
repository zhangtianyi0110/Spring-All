package com.ty.controller;

import com.ty.config.JwtProperties;
import com.ty.constant.SecurityConsts;
import com.ty.pojo.ResponseData;
import com.ty.pojo.User;
import com.ty.redis.JwtRedisCache;
import com.ty.service.UserService;
import com.ty.shiro.JwtUtil;
import com.ty.util.MD5Utils;
import com.ty.util.ResponseUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class LoginController {
    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private JwtRedisCache jwtRedisCache;
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private UserService userService;

    @GetMapping("/login")
    public String login(){
        //如果已经认证通过，直接跳转到首页
        if (SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/index";
        }
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseData login(User user) throws Exception{
        log.info("开始认证...");
        String username = user.getUsername();
        String password = MD5Utils.encrypt(user.getPassword(),username);
        String realPassword = userService.findByUsername(username).getPassword();
        if (realPassword == null) {
            throw new UnknownAccountException();
        } else if (!realPassword.equals(password)) {
            throw new IncorrectCredentialsException();
        }
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        String jwt = JwtUtil.sign(user.getUsername(), currentTimeMillis);//生成签名
        jwtRedisCache.put(SecurityConsts.REFRESH_TOKEN + username, currentTimeMillis, jwtProperties.getRefreshTokenExpireTime());//将时间戳存入缓存
        log.info("结束认证...");
        return ResponseUtil.success("登录成功", jwt);
    }

    @RequestMapping("/logout")
    public String logout(){
        return "login";
    }

    @RequestMapping("/")
    public String redirectIndex(){
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String index(Model model) {
        String token = (String) SecurityUtils.getSubject().getPrincipal();
        User user = userService.findByUsername(JwtUtil.getUsername(token));
        model.addAttribute("user", user);
        return "index";
    }
    @GetMapping("/403")
    public String forbid() {
        return "403";
    }
}

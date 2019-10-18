package com.zty.controller;

import com.zty.base.BaseController;
import com.zty.constant.SecurityConsts;
import com.zty.pojo.ResponseData;
import com.zty.pojo.User;
import com.zty.service.UserService;
import com.zty.shiro.jwt.JwtConfig;
import com.zty.shiro.jwt.JwtRedisCache;
import com.zty.shiro.jwt.JwtUtil;
import com.zty.util.MD5Utils;
import com.zty.util.ResponseUtil;
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
public class LoginController extends BaseController {
    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private JwtRedisCache jwtRedisCache;
    @Resource
    private JwtConfig jwtConfig;
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
        jwtRedisCache.put(SecurityConsts.REFRESH_TOKEN + username, currentTimeMillis, jwtConfig.getRefreshTokenExpireTime());//将时间戳存入缓存
        jwtRedisCache.put(SecurityConsts.IP_TOKEN + username, JwtUtil.getIpAddress(request), jwtConfig.getRefreshTokenExpireTime());//将ip存入，防止其他用户使用token侵入
        log.info("当前用户登录ip为:"+JwtUtil.getIpAddress(request));
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

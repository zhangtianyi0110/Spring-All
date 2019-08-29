package com.ty.controller;

import com.ty.pojo.ResponseData;
import com.ty.pojo.User;
import com.ty.service.UserRoleService;
import com.ty.util.ResponseUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;

@RestController
public class RoleCtoller {
    @Resource
    private UserRoleService userRoleService;

    @GetMapping("")
    public ResponseData getRoles(){
        Set<String> roles = userRoleService.getRoleByUsername(((User)SecurityUtils.getSubject().getPrincipal()).getUsername());
        return ResponseUtil.success("ok", roles);
    }
    @PostMapping("")
    public ResponseData postRoles(){

        return null;
    }
}

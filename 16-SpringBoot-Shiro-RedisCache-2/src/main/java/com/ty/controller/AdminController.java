package com.ty.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdminController {

    @RequiresPermissions("admin:get")
    @RequestMapping("get")
    public String adminGet(Model model) {
        model.addAttribute("value", "获取管理员信息");
        return "admin";
    }

    @RequiresPermissions("admin:delete")
    @RequestMapping("delete")
    public String adminDelete(Model model) {
        model.addAttribute("value", "删除管理员");
        return "admin";
    }
}

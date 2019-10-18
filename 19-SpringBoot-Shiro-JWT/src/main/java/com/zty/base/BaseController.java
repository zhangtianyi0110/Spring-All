package com.zty.base;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class BaseController {

    @Autowired
    protected HttpServletRequest request;
    @Resource
    protected HttpServletResponse response;
    @Autowired
    protected HttpSession session;
}

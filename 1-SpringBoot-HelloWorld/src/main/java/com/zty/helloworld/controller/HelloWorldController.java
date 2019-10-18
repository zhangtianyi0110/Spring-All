package com.zty.helloworld.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Title: SpringAll</p >
 * <p>Description: TODO(用一句话描述该文件做什么)</p >
 * <p>Copyright: Copyright (c) 2019</p >
 * <p>Company: zty</p >
 *
 * @author zhangtianyi
 * @version V1.0
 * @create 2019/10/11 11:25
 */
@RestController
public class HelloWorldController {
    @RequestMapping("hello")
    public String helloworld(){
        return "Hello World !";
    }
}

package com.ty.controller;

import com.ty.bean.ResponseBean;
import com.ty.exception.MyException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    //模拟数据库数据
    private Map<String,String> map = new HashMap<>();
    {
        map.put("ty","123");
    }

    @PostMapping("/login")
    public ResponseBean login(String username, String password){
        if(map.get(username)!=null&&(map.get(username).equals(password))){
            return new ResponseBean(200,"登录成功",map);
        }else {
            throw new MyException(401,"登录失败");
        }
    }
}

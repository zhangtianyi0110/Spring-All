package com.ty.service;

import com.ty.dao.UserMapper;
import com.ty.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public User findByUsername(String username){
        return userMapper.findByUsername(username);
    }
    public boolean checkUser(User user){
        if(userMapper.checkUser(user)>0){
            return true;
        }
        return false;
    }

}

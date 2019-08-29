package com.ty.service;

import com.ty.dao.UserRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class UserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;

    public Set<String> getRoleByUsername(String username){
        return userRoleMapper.findRolesByUsername(username);
    }
}

package com.ty.service;

import com.ty.dao.UserRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class UserRoleService {
    private Logger logger = LoggerFactory.getLogger(UserRoleService.class);
    @Resource
    private UserRoleMapper userRoleMapper;

    public Set<String> getRoleByUsername(String username){
        return userRoleMapper.findRolesByUsername(username);
    }
}

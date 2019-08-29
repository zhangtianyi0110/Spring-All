package com.ty.service;

import com.ty.dao.UserPermsMapper;
import com.ty.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class UserPermsService {
    @Resource
    private UserPermsMapper userPermsMapper;

    public Set<String> getPermsByusername(User user){
        Set<String> permissions = userPermsMapper.findPermsByusername(user);
        return permissions;
    }
}

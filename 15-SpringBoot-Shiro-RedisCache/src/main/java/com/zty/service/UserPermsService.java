package com.zty.service;

import com.zty.dao.UserPermsMapper;
import com.zty.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class UserPermsService {
    @Resource
    private UserPermsMapper userPermsMapper;

    public Set<String> getPermsByusername(User user){
        Set<String> permissions = userPermsMapper.findPermsByUsername(user);
        return permissions;
    }
}

package com.zty.dao;

import com.zty.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface UserPermsMapper {
    Set<String> findPermsByUsername(User user);
}

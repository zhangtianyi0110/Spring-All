package com.ty.dao;

import com.ty.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface UserPermsMapper {
    Set<String> findPermsByusername(User user);
}

package com.ty.dao;

import com.ty.pojo.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    List<Role> findRolesByusername(String username);
}

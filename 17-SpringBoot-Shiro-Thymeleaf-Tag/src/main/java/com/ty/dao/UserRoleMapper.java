package com.ty.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

@Mapper
public interface UserRoleMapper {
    Set<String> findRolesByUsername(String username);
}

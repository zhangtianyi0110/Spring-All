package com.ty.dao;

import com.ty.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserPermissionMapper {
    List<Permission> findPermsByusername(String username);
}

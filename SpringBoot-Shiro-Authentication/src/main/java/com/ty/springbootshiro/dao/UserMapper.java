package com.ty.springbootshiro.dao;

import com.ty.springbootshiro.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    User findByUserName(String username);

    @Select("select 1 from user where username=#{username} and password=#{password}")
    Integer checkUser(User user);
}

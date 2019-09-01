package com.ty;

import com.ty.pojo.User;
import com.ty.service.UserPermsService;
import com.ty.service.UserRoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
    @Resource
    private UserPermsService userPermsService;
    @Resource
    private UserRoleService userRoleService;

    @Test
    public void contextLoads() {
    }




    @Test
    public void getPermsByusername(){
        User user = new User();
        user.setUsername("admin");
        Set<String> permissions = userPermsService.getPermsByusername(user);
        permissions.forEach(permission->{
            System.out.println(permission);
        });
    }
    @Test
    public void getRoleByUsername(){
        Set<String> roles = userRoleService.getRoleByUsername("admin");
        roles.forEach(role->{
            System.out.println(role.toString());
        });
    }


}

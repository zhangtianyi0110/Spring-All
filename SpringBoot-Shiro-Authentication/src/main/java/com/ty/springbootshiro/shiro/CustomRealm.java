package com.ty.springbootshiro.shiro;

import com.ty.springbootshiro.dao.UserMapper;
import com.ty.springbootshiro.pojo.User;
import com.ty.springbootshiro.util.MD5Utils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @ClassName: CustomRealm
 * @Description: 自定义Realm
 * @author zhangtainyi
 * @date 2019/8/27 15:29
 *
 */
public class CustomRealm extends AuthorizingRealm {

    private Logger log = LoggerFactory.getLogger(CustomRealm.class);
    @Resource
    private UserMapper userMapper;
    //登录认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.从主体传过来的授权信息中，获取用户名
        String userName = (String) authenticationToken.getPrincipal();
        String password = String.copyValueOf((char[]) authenticationToken.getCredentials());
        password = MD5Utils.encrypt(password,userName);
        //2.通过用户名到数据库中获取角色权限数据
        User user = userMapper.findByUserName(userName);
        if(user == null){
            throw new UnknownAccountException("用户名或密码错误");
        }
        if(password!=null && !password.equals(user.getPassword())){
            throw new IncorrectCredentialsException("用户名或密码错误");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName,password,getName());
        //返回authenticationInfo对象前设置盐
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(user.getUsername()));
        return authenticationInfo;

    }
    //获取权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
}

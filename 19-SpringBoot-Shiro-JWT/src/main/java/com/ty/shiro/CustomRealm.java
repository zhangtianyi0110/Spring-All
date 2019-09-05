package com.ty.shiro;

import com.ty.constant.SecurityConsts;
import com.ty.pojo.User;
import com.ty.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
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

    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Resource
    private UserService userService;
//    @Resource
//    private UserRoleService userRoleService;
//    @Resource
//    private UserPermsService userPermsService;

    /**
     * 大坑，需要重写这个方法
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }


    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("-----doGetAuthenticationInfo 开始-----");
        // 这里的 token是从 JWTFilter 的 executeLogin 方法传递过来的
        String token = (String) authenticationToken.getCredentials();
        //1.从token中获取用户名，因为用户名不是私密直接获取
        String usernmae = JWTUtil.getUsername(token);
        //2.通过用户名到数据库中获取角色权限数据
        User user = userService.findByUsername(usernmae);
        if(user == null || !JWTUtil.verify(token, usernmae, user.getPassword())){
            throw new UnknownAccountException("用户名或密码错误");
        }
        String refreshTokenCacheKey = SecurityConsts.PREFIX_SHIRO_REFRESH_TOKEN + usernmae;

        if (cacheClient.exists(refreshTokenCacheKey)) {
            String currentTimeMillisRedis = cacheClient.get(refreshTokenCacheKey);
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (JwtUtil.getClaim(token, SecurityConsts.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, "shiroRealm");
            }
        }
        throw new AuthenticationException("Token expired or incorrect.");
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(token, token, getName());
        log.info("-----doGetAuthenticationInfo 结束-----");
        return authenticationInfo;

    }
    //获取权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1.从主体传过来的认证信息中，获取用户对象
//        User user = (User)principalCollection.getPrimaryPrincipal();
        //通过用户名到数据库获取角色和权限
//        Set<String> roles = userRoleService.getRoleByUsername(user.getUsername());
//        Set<String> permissions = userPermsService.getPermsByusername(user);
//        //构造对象返回加上角色权限
//        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        authorizationInfo.setRoles(roles);//角色
//        authorizationInfo.setStringPermissions(permissions);//权限
//        return authorizationInfo;
        return null;

    }
}

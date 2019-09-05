package com.ty.shiro;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:properties/jwt.properties")
@Component
public class JwtProperties {
    //token过期时间，单位分钟
    @Value("${token.tokenExpireTime}")
    private Integer tokenExpireTime;
    
    //刷新Token过期时间，单位分钟
    @Value("${token.refreshTokenExpireTime}")
    private Integer refreshTokenExpireTime;
    
    //Shiro缓存有效期，单位分钟
    @Value("${token.shiroCacheExpireTime}")
    private Integer shiroCacheExpireTime;
    
    //token加密密钥
    @Value("${token.secretKey}")
    private String secretKey;

    public Integer getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(Integer tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }

    public Integer getRefreshTokenExpireTime() {
        return refreshTokenExpireTime;
    }

    public void setRefreshTokenExpireTime(Integer refreshTokenExpireTime) {
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public Integer getShiroCacheExpireTime() {
        return shiroCacheExpireTime;
    }

    public void setShiroCacheExpireTime(Integer shiroCacheExpireTime) {
        this.shiroCacheExpireTime = shiroCacheExpireTime;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
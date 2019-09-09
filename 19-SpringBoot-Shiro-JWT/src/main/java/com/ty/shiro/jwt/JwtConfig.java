package com.ty.shiro.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:properties/jwt.properties")
@ConfigurationProperties(prefix = "token")
@Configuration
public class JwtConfig {
    //token过期时间，单位分钟
//    @Value("${token.tokenExpireTime}")
    private Integer tokenExpireTime;
    
    //刷新Token过期时间，单位分钟
//    @Value("${token.refreshTokenExpireTime}")
    private Integer refreshTokenExpireTime;
    
    //Shiro缓存有效期，单位分钟
//    @Value("${token.shiroCacheExpireTime}")
    private Integer shiroCacheExpireTime;
    
    //token加密密钥
//    @Value("${token.secretKey}")
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
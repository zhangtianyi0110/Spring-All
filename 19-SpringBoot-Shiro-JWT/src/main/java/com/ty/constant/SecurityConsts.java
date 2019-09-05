package com.ty.constant;

public class SecurityConsts {

    //request请求头属性
    public static final String REQUEST_AUTH_HEADER="Authorization";

    //JWT-username
    public static final String USERNAME = "username";

    //Shiro redis 前缀
    public static final String PREFIX_SHIRO_CACHE = "tyblog:cache:";

    //redis-key-前缀-shiro:refresh_token
    public final static String PREFIX_SHIRO_REFRESH_TOKEN = "tyblog:refresh_token:";

    //JWT-currentTimeMillis
    public final static String CURRENT_TIME_MILLIS = "currentTimeMillis";
}
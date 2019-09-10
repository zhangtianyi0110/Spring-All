package com.ty.constant;

public class SecurityConsts {

    //request请求头属性
    public static final String REQUEST_AUTH_HEADER="Authorization";

    //JWT-username
    public static final String USERNAME = "username";

    //shiro_redis 前缀
    public static final String PREFIX_SHIRO_CACHE = "shiro:cache";

    //refresh_token前缀
    public final static String REFRESH_TOKEN = "refresh_token:";

    //ip_token 记录token的ip
    public final static String IP_TOKEN = "ip_token:";

    //JWT-currentTimeMillis
    public final static String CURRENT_TIME_MILLIS = "currentTimeMillis";
}
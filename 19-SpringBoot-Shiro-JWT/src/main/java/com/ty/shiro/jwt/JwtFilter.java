package com.ty.shiro.jwt;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ty.constant.SecurityConsts;
import com.ty.pojo.ResponseData;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

/**
 *  @ClassName: JwtFilter
 *  @Description: JWT过滤器
 *  @author: zhangtianyi
 *  @Date: 2019-09-04 21:28
 *
 */

public class JwtFilter extends BasicHttpAuthenticationFilter {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String AUTHORIZATION = "Authorization";

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    private JwtRedisCache jwtRedisCache;

    private JwtConfig jwtConfig;

    public JwtFilter() {
    }

    public JwtFilter(JwtRedisCache jwtRedisCache, JwtConfig jwtConfig) {
        this.jwtRedisCache = jwtRedisCache;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
              Object mappedValue) throws UnauthorizedException {

        //判断请求的请求头是否带上 "Token"
        if (isLoginAttempt(request, response)) {
            //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            try {
                this.executeLogin(request, response);
            } catch (Exception e) {
                String msg = e.getMessage();
                Throwable throwable = e.getCause();
                if (throwable != null && throwable instanceof SignatureVerificationException) {
                    msg = "Token或者密钥不正确(" + throwable.getMessage() + ")";
                } else if (throwable != null && throwable instanceof TokenExpiredException) {
                    // AccessToken已过期,但在刷新期内，刷新token
                    if (this.refreshToken(request, response)) {
                        return true;
                    } else {
                        msg = "Token已过期(" + throwable.getMessage() + ")";
                    }
                } else {
                    if (throwable != null) {
                        msg = throwable.getMessage();
                    }
                }
                //token 错误
                log.error("认证不通过，请重新登录！");
                this.response401(request,response,msg);
                return false;
            }
        }
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        return true;
    }
    /**
     * 判断用户是否想要登入。
     * 检测 header 里面是否包含 token 字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(AUTHORIZATION);
        return token != null;
    }

    /**
     * 执行登录操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(AUTHORIZATION);
        JwtToken jwtToken = new JwtToken(token);

        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;

    }

    /**
     * 刷新AccessToken，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        // 获取Token(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        // 获取当前Token的帐号信息
        String username = JwtUtil.getClaim(token, SecurityConsts.USERNAME);
        String refreshTokenCacheKey = SecurityConsts.REFRESH_TOKEN + username;
        // 判断Redis中RefreshToken是否存在
        if (jwtRedisCache.get(refreshTokenCacheKey) != null) {
            // 获取RefreshToken时间戳,及Token中的时间戳
            // 相比如果一致，进行Token刷新
            String currentTimeMillisRedis = (String) jwtRedisCache.get(refreshTokenCacheKey);
            String tokenMillis = JwtUtil.getClaim(token, SecurityConsts.CURRENT_TIME_MILLIS);

            if (tokenMillis.equals(currentTimeMillisRedis)) {

                // 设置RefreshToken中的时间戳为当前最新时间戳,并重制ip缓存时间
                String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                jwtRedisCache.put(refreshTokenCacheKey, currentTimeMillis, jwtConfig.getRefreshTokenExpireTime());
                jwtRedisCache.put(SecurityConsts.IP_TOKEN+username, JwtUtil.getIpAddress((HttpServletRequest) request), jwtConfig.getRefreshTokenExpireTime());
                // 刷新AccessToken，为当前最新时间戳
                token = JwtUtil.sign(username, currentTimeMillis);

                // 使用AccessToken 再次提交给ShiroRealm进行认证，如果没有抛出异常则登入成功，返回true
                JwtToken jwtToken = new JwtToken(token);
                this.getSubject(request, response).login(jwtToken);

                // 设置响应的Header头新Token
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader(SecurityConsts.REQUEST_AUTH_HEADER, token);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", SecurityConsts.REQUEST_AUTH_HEADER);
                return true;
            }
        }
        return false;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 401非法请求
     * @param req
     * @param resp
     */
    private void response401(ServletRequest req, ServletResponse resp,String msg) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out =httpServletResponse.getWriter()){

            ResponseData result = new ResponseData();
            result.setCode(401);
            result.setMsg(msg);
            out.append(new ObjectMapper().writeValueAsString(result));
        } catch (IOException e) {
            log.error("返回Response信息出现IOException异常:" + e.getMessage());
        }
    }

    /**
     * 将非法请求跳转到 /unauthorized/**
     */
    private void responseError(ServletResponse response, String message) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            //设置编码，否则中文字符在重定向时会变为空字符串
            message = URLEncoder.encode(message, "UTF-8");
            httpServletResponse.sendRedirect("/unauthorized/" + message);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

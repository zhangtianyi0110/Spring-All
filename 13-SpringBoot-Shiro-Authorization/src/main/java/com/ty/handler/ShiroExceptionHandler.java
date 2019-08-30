package com.ty.handler;

import com.ty.pojo.ResponseData;
import com.ty.util.ResponseUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class ShiroExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ShiroExceptionHandler.class);

    /**
     * 对shiro认证抛出的异常统一处理
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseData handleAuthenticationException(HttpServletRequest request,
             HttpServletResponse response, Exception e) throws IOException {

        //系统异常打印
        logger.error(e.getMessage());
        if(e instanceof UnknownAccountException){
            return ResponseUtil.failure(401,"用户名不存在");
        }else if(e instanceof IncorrectCredentialsException){
            return ResponseUtil.failure(401,"用户名或密码错误");
        }
        return ResponseUtil.failure(401,"认证失败！");//异常回传信息
    }
    /**
     * 对shiro授权抛出的异常统一处理
     */
    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public String handleAuthorizationException(HttpServletRequest request,
             HttpServletResponse response, Exception e) throws IOException {
        //系统异常打印
        logger.error(e.getMessage());

        return "403";
    }

}

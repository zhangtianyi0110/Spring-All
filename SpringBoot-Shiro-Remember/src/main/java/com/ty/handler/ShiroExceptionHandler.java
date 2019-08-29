package com.ty.handler;

import com.ty.pojo.ResponseData;
import com.ty.util.ResponseUtil;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class ShiroExceptionHandler {
    private Logger log = LoggerFactory.getLogger(ShiroExceptionHandler.class);

    @ExceptionHandler(ShiroException.class)
    public ResponseData MyExcepitonHandler(HttpServletRequest request,
             HttpServletResponse response, Exception e) throws IOException {
        if(e instanceof UnknownAccountException){
            return ResponseUtil.failure(401,"用户名不存在");
        }else if(e instanceof IncorrectCredentialsException){
            return ResponseUtil.failure(401,"用户名或密码错误");
        }else if(e instanceof AuthenticationException){
            return ResponseUtil.failure(401,"认证失败！");
        }
        //系统异常打印
        log.error(e.getMessage());
        e.printStackTrace();
        return ResponseUtil.failure(401,e.getMessage());//异常回传信息
    }
}

package com.ty.shiro;

import org.apache.shiro.authc.AuthenticationToken;


public class JWTToken implements AuthenticationToken {

    private String token;

    private String exipreAt;

    public JWTToken() {
    }

    public JWTToken(String token) {
        this(token,null);
    }

    public JWTToken(String token, String exipreAt) {
        this.token = token;
        this.exipreAt = exipreAt;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExipreAt() {
        return exipreAt;
    }

    public void setExipreAt(String exipreAt) {
        this.exipreAt = exipreAt;
    }
}

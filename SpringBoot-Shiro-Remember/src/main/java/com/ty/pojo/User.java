package com.ty.pojo;


public class User {

  private long id;
  private String username;
  private String password;
  private boolean isRememberMe;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean getIsRememberMe() {
    return isRememberMe;
  }

  public void setRememberMe(boolean rememberMe) {
    isRememberMe = rememberMe;
  }
}

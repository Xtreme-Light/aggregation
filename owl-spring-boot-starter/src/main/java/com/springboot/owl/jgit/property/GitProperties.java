package com.springboot.owl.jgit.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "owl.git")
public class GitProperties {

  private boolean enable;
  private String userName;

  private String password;


  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}

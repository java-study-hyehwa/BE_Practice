package server.controller;

import lombok.Getter;

@Getter
public class RequestDto {

  private String username;
  private String password;

  public RequestDto() {
    // TODO document why this constructor is empty
  }

  public RequestDto setUsername(String username) {
    this.username = username;
    return this;
  }

  public RequestDto setPassword(String password) {
    this.password = password;
    return this;
  }


  public RequestDto build() {
    return this;
  }

}

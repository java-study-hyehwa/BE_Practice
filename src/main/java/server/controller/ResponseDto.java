package server.controller;

import lombok.Getter;
import server.http.HttpStatus;

@Getter
public class ResponseDto {

  private boolean loginSuccess;
  private HttpStatus status;

  public ResponseDto() {
    // TODO document why this constructor is empty
  }

  public ResponseDto setLoginSuccess(boolean loginSuccess) {
    this.loginSuccess = loginSuccess;
    return this;
  }

  public ResponseDto setStatus(HttpStatus status) {
    this.status = status;
    return this;
  }

  public ResponseDto build() {
    return this;
  }
}

package server.service;

import server.controller.RequestDto;
import server.controller.ResponseDto;
import server.domain.user.UserModel;
import server.security.PasswordEncryption;

public class LoginService {

  private final UserModel userModel = new UserModel();

  public ResponseDto loginRequest(RequestDto requestDto) {
    String username = requestDto.getUsername();
    String password = requestDto.getPassword();
    byte[] passwordHash = PasswordEncryption.hashPassword(password);
    return userModel.loginLogic(username, passwordHash);
  }
}

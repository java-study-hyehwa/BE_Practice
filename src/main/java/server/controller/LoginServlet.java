package server.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import server.service.LoginService;

@WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

  private LoginService loginService;


  public LoginServlet() {
  }

  @Override
  public void init() throws ServletException {
    this.loginService = new LoginService(); // 의존성 주입
    System.out.println("Servlet " + this.getServletName() + " has started");
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    String usernameInput = req.getParameter("username");
    String passwordInput = req.getParameter("password");
    RequestDto requestDto = new RequestDto().
        setUsername(usernameInput).
        setPassword(passwordInput).build();
    ResponseDto responseDto = loginService.loginRequest(requestDto);
    System.out.println(responseDto.isLoginSuccess() + " " + responseDto.getStatus());
    if (responseDto.isLoginSuccess()) {
      resp.sendRedirect("/");
    }


  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    super.service(req, resp);
  }

  public void setLoginService(LoginService loginService) {
    this.loginService = loginService;
  }
}

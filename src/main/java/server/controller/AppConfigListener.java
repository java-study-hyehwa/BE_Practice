package server.controller;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import server.service.LoginService;

@WebListener
public class AppConfigListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    LoginService loginService = new LoginService(); // LoginService 인스턴스 생성
    LoginServlet loginController = new LoginServlet(); // LoginController 인스턴스 생성
    loginController.setLoginService(loginService); // 세터를 통한 의존성 주입

    sce.getServletContext().setAttribute("loginController", loginController);
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // 필요한 종료 작업 수행
  }

}

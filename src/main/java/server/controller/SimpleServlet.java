package server.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.net.URL;


@WebServlet(urlPatterns = "/simple")
public class SimpleServlet extends HttpServlet {

  @Serial
  private static final long serialVersionUID = 1L;

  public SimpleServlet() {
    super();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    // 조회 작업 수행
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    // 생성 작업 수행
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
    // 수정 작업 수행
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
    // 삭제 작업 수행
  }

  @Override
  public void service(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    PrintWriter writer = resp.getWriter();

    URL url = Thread.currentThread().getContextClassLoader()
        .getResource(this.getClass().getName() + ".class");
    assert url != null;
    writer.append(url.getPath());

    RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/simple.jsp");
    dispatcher.forward(req, resp);
    writer.println("Hello, World!");
  }

}

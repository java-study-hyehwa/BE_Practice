package server.domain.user;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Scanner;
import server.controller.ResponseDto;
import server.db.ConnectionFactory;
import server.domain.user.banker.Banker;
import server.domain.user.customer.Customer;
import server.http.HttpStatus;

public class UserModel {

  public UserModel() {
    // TODO document why this constructor is empty
  }

  public ResponseDto loginLogic(String username, byte[] password) {
    return loginLogicHelper(username, password);
  }

  private ResponseDto loginLogicHelper(String username, byte[] password) {
    System.out.println("Login Logic 호출");
    ResultSet rs;
    String query = "SELECT * FROM customer WHERE customer_name = ?";
    try (Connection conn = ConnectionFactory.INSTANCE.getConnection()) {
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, username);
      rs = ps.executeQuery();
      if (rs.next()) {
        if (Arrays.equals(rs.getBytes("password"), password)) {
          System.out.println("Login success");
          return new ResponseDto()
              .setLoginSuccess(true)
              .setStatus(HttpStatus.OK).build();
        } else {
          System.out.println("Login failed");
        }
      }
    } catch (Exception e) {
      System.out.println("Login failed2");
      e.printStackTrace();
    }
    return new ResponseDto()
        .setLoginSuccess(false)
        .setStatus(HttpStatus.NOT_FOUND)
        .build();

  }

  void registerLogic(User user) {

  }

  void logoutLogic(User user) {

  }

  void findUserLogic(User user) {

  }


  public void customerLogic(Customer customer) {
    Scanner sc = new Scanner(System.in);
    int choice = sc.nextInt();
    System.out.println("Customer Logic");
    System.out.println("-----------------");
    System.out.println("1. Login");
    System.out.println("2. Register");
    System.out.println("3. Logout");
    switch (choice) {
      case 1:
        break;
      case 2:
        registerLogic(customer);
        break;
      case 3:
        logoutLogic(customer);
        break;
      case 4:
        findUserLogic(customer);
        break;
    }

  }

  public void bankerLogic(Banker banker) {
    Scanner sc = new Scanner(System.in);
    int choice = sc.nextInt();
    System.out.println("Banker Logic");
    System.out.println("-----------------");
    System.out.println("1. Login");
    System.out.println("2. Register");
    System.out.println("3. Logout");
    System.out.println("4. AdminMenu");
    switch (choice) {
      case 1:
        break;
      case 2:
        registerLogic(banker);
        break;
      case 3:
        logoutLogic(banker);
        break;
      case 4:
        AdminMenu();
        break;
    }
  }

  private void AdminMenu() {

  }

  public void method() {
  }
}

package community.feature;

import community.db.ConnectionFactory;
import community.exceptions.GlobalExceptionConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AccountSearch {
  private final Scanner sc;
  public AccountSearch(Scanner sc){
    this.sc = sc;
    System.out.println("AccountSearch created!");
    System.out.println("pleas input the account number you want to search: ");
  }
  public void run(){
    accountCheck();
  }

  private void accountCheck() {
    String accountNum = sc.nextLine();

    PreparedStatement ps;
    ResultSet rs;
    String sql;
    sql = "SELECT * FROM account WHERE account_num = ?";
    try(Connection con = ConnectionFactory.getConnection()){
      ps = con.prepareStatement(sql);
      ps.setString(1, accountNum);
      rs = ps.executeQuery();
      if(rs.next()){
        System.out.println("Account Number: " + rs.getString("account_num"));
      }

    }catch(Exception e){
      GlobalExceptionConfig.log(e);
    }
  }

}
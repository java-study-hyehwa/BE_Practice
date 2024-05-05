package domain.user;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import view.Status;
import common.repository.DriverConnector;
import controller.*;



public class UserService {

    Scanner sc = new Scanner(System.in);
    DriverConnector driverConnector = new DriverConnector();
    UserDao dao = new UserDao();


    public Status signUp(Status status, User user) throws SQLException {
        Connection con = null;
        String resultMessage;

        // SQL 실행
        con = driverConnector.connectDriver();
        try {
            ArrayList<User> userCheck = dao.selectCheckExistUser(con, user.getuserId());
            if (!userCheck.isEmpty()){
                status.setMessage(Message.ERROR_WRONG_USER_SIGNUP.getMessage());
                return status;
            }
            resultMessage = dao.insertSignUp(con, user);
            status.setMessage(resultMessage);
            status.setUserId(Tag.OLD_GUEST.getTag());
            status.setWorkFlow(Tag.CONTINUE);
            status.setPageTag(Tag.LOG_IN);
        } finally {
            if(con!=null) con.close();
        }
        return status;
    }


    public Status logIn(Status status) throws SQLException {
        Connection con = null;
        ArrayList<User> userList;
        User user = (User) status.getDataValue(Tag.TRY_LOGIN_USER.getTag());

        // SQL 실행
        con = driverConnector.connectDriver();
        try {
            userList = dao.selectLogIn(con, user.getuserId(), user.getPassword());
            if (!userList.isEmpty()){
                status.setUserId(userList.getFirst().getuserId());
                status.setMessage(Message.INFO_SUCCESS_LOGIN.getMessage(userList.getFirst().getName()));
                status.setWorkFlow(Tag.CONTINUE);
                status.setPageTag(Tag.MAIN);
            } else if (userList.isEmpty()){
                status.setMessage(Message.ERROR_FAILED_LOGIN.getMessage());
            }
        } finally {
            if(con!=null) con.close();
        }

        return status;
    }


    public Status myPage(Status status) throws SQLException {
        Connection con = null;
        ArrayList<User> userList;

        // SQL 실행
        con = driverConnector.connectDriver();
        try {
            userList = dao.selectMyInfo(con, status.getUserId());
        } finally {
            if(con!=null) con.close();
        }
        status.setDataValue(Tag.PUT_DATA,Tag.RESULT_USER.getTag(),userList.getFirst());

        return status;
    }


/*    public ArrayList<User> selectAll() throws SQLException {
        Connection con = null;
        ArrayList<User> list;

        // SQL 실행
        con = driverConnector.connectDriver();
        try {
            list = dao.selectAll(con);
        } finally {
            if(con!=null) con.close();
        }
        return list;
    }*/


}
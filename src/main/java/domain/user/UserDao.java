package domain.user;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import controller.Message;
import common.repository.Query;

public class UserDao {


    public ArrayList<User> selectLogIn(Connection con, String userId, String password) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<User> userlist = new ArrayList<>();
        try {
            String sql = Query.USER_SELECT_LOGIN.getQueryString();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userId = rs.getString(1);
                userlist.add(new User(userId));
            }
        } finally {
            if(rs!=null) rs.close();
            if(pstmt!=null) pstmt.close();
        }
        return userlist;
    }

    public ArrayList<User> selectCheckExistUser(Connection con, String userId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<User> userlist = new ArrayList<>();
        try {
            String sql = Query.USERL_SELECT_CHECK_EXIST.getQueryString();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userId = rs.getString(1);
                userlist.add(new User(userId));
            }
        } finally {
            if(rs!=null) rs.close();
            if(pstmt!=null) pstmt.close();
        }
        return userlist;
    }



    public ArrayList<User> selectMyInfo(Connection con, String userId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<User> userlist = new ArrayList<>();
        try {
            String sql = Query.USER_SELECT_MY_INFO.getQueryString();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                LocalDate birthDate = rs.getTimestamp(2)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                char gender = rs.getString(3).charAt(0);
                String address = rs.getString(4);
                String phoneNum = rs.getString(5);
                String job = rs.getString(6);
                LocalDate signUpDate = rs.getTimestamp(7)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                userlist.add(new User(name, birthDate, gender, address, phoneNum, job, signUpDate));
            }
        } finally {
            if(rs!=null) rs.close();
            if(pstmt!=null) pstmt.close();
        }
        return userlist;
    }

    public String insertSignUp(Connection con, User user) throws SQLException {
        PreparedStatement pstmt = null;
        boolean result;
        String resultMessage;
        try {
            String sql = Query.USER_INSERT_SIGNUP.getQueryString();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3,user.getName());
            pstmt.setTimestamp(4,Timestamp.valueOf(user.getBirth_date().atTime(0,0,0)));
            pstmt.setString(5,String.valueOf(user.getGender()));
            pstmt.setString(6, user.getAddress());
            pstmt.setString(7, user.getPhoneNumber());
            pstmt.setString(8, user.getJob());
            pstmt.setInt(9, user.getAnnualSalary());
            result = pstmt.execute(); // 성공시 false, 실패시 true
            if (!result) {
                resultMessage = Message.INFO_SUCCESS_USER_SIGNUP.getMessage(user.getUserId());
            } else {
                resultMessage = Message.ERROR_WRONG_USER_SIGNUP.getMessage();
            }
        } finally {
            con.close();
            if(pstmt!=null) pstmt.close();
        }
        return resultMessage;
    }

    public String deleteWithdrawalUser (Connection con, User user)throws SQLException {
        PreparedStatement pstmt = null;
        boolean result;
        String resultMessage;
        try {
            String sql = Query.USER_DELETE_WITHDRAWAL.getQueryString();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3,user.getName());
            result = pstmt.execute(); // 성공시 false, 실패시 true
            if (!result) {
                resultMessage = Message.INFO_SUCCESS_USER_WITHDRAWAL.getMessage(user.getUserId());
            } else {
                resultMessage = Message.ERROR_WRONG_USER_WITHDRAWAL.getMessage();
            }
        } finally {
            con.close();
            if(pstmt!=null) pstmt.close();
        }
        return resultMessage;
    }

}
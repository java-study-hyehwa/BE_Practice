package domain.account;

import view.Status;
import controller.Message;
import controller.Tag;
import domain.user.UserDao;
import common.repository.rdsDriverConnectorImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;

public class AccountService {
    Scanner sc = new Scanner(System.in);
    rdsDriverConnectorImpl driverConnector = new rdsDriverConnectorImpl();
    AccountDao dao = new AccountDao();
    UserDao userDao = new UserDao();


    public Status insertOneAccount(Status status) throws SQLException {
        Connection con = null;
        Account account = new Account();
        ArrayList<Account> accountList;
        String accountNum = "";

        con = driverConnector.connectDriver();
        try {
            // 계좌번호 난수 생성하고 중복체크, 중복시 루프
            do {
                for (int i = 0; i < 10; i++) {
                    accountNum += (int) ((Math.random() * (10 - 0)) + 0);
                }
                accountList = dao.selectOne(con, accountNum);
            } while (!accountList.isEmpty());

            // 생성할 계좌 정보 설정
            String accountType = (String) status.getDataValue(Tag.ACCOUNT_TYPE.getTag());
            account.setUserId(status.getUserId());
            account.setaccountNum(accountNum);
            account.setProductType(accountType);

            // 신규 계좌 insert
            status.setMessage(dao.insertOneAccount(con, account));
            status.setPageTag(Tag.MANAGE_ACCOUNTS);
            status.setWorkFlow(Tag.CONTINUE);

            return status;
        } finally {
            if(con!=null) con.close();
        }
    }

    public Status selectMyAccounts(Status status) throws SQLException {
        Connection con = null;
        ArrayList<Account> accountList;

        con = driverConnector.connectDriver();
        try {
            accountList = dao.selectMyAccounts(con, status.getUserId());
        } finally {
            if(con!=null) con.close();
        }

        status.setDataValue(Tag.PUT_DATA, Tag.RESULT_ACCOUNT_LIST.getTag(),accountList);

        return status;
    }



    public Status deleteClosedAccount(Status status) throws SQLException {
        Connection con = null;

        con = driverConnector.connectDriver();
        try {
            // 입력받은 계좌번호 유효성 검증
            String accountNum = (String) status.getDataValue(Tag.ACCOUNT_NUMBER.getTag());
            ArrayList<Account> accountCheck = dao.selectOne(con, accountNum);

            if (accountCheck.isEmpty()) {
                status.setMessage(Message.ERROR_WRONG_ACCOUNT.getMessage());
                return status;
            }
            // 해지할 계좌 delete
            accountNum = (String) status.getDataValue(Tag.ACCOUNT_NUMBER.getTag());
            status.setMessage(dao.deleteOneAccount(con, accountNum));
            status.setPageTag(Tag.MANAGE_ACCOUNTS);
            status.setWorkFlow(Tag.CONTINUE);

            return status;
        } finally {
            if(con!=null) con.close();
        }
    }


}

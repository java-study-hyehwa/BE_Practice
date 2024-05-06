package domain.trade;

import domain.account.Account;
import domain.account.AccountDao;
import view.Status;
import controller.Message;
import controller.Tag;
import common.repository.rdsDriverConnectorImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class TradeService {

    rdsDriverConnectorImpl driverConnector = new rdsDriverConnectorImpl();
    AccountDao accountDao = new AccountDao();
    TradeDao dao = new TradeDao();

    public Status insert(Status status) throws SQLException {
        Connection con = null;
        ArrayList<Account> targetList;
        Trade trade = (Trade) status.getDataValue(Tag.NEW_TRADE.getTag());

        // 신규 입/출금 Trade객체 유효성 검증
        con = driverConnector.connectDriver();
        try {
            targetList = accountDao.selectOne(con, trade.getTargetAccount());
        } finally {
            if(con!=null) con.close();
        }

        if (targetList.isEmpty()) {
            status.setMessage(Message.ERROR_WRONG_ACCOUNT.getMessage());
            return status;
        }

        int newTarBalance = 0;
        if (trade.getAction().equals(Tag.ACTION_DEPOSIT)) {
            newTarBalance = targetList.getFirst().getBalance() + trade.getAmount();
        } else if (trade.getAction().equals(Tag.ACTION_WITHDRAW)){
            newTarBalance = targetList.getFirst().getBalance() - trade.getAmount();
        }

        if (trade.getAction().equals(Tag.ACTION_WITHDRAW) && newTarBalance < 0) {
            status.setMessage(Message.ERROR_FAILED_TRANSFER_OVER_BALANCE.getMessage(Tag.ACTION_WITHDRAW.getTag()));
            return status;
        }
        trade.setTarBalance(newTarBalance);

        // 신규 입/출금 Trade insert 후, 거래 결과 account에 update
        String resultMessage;
        con = driverConnector.connectDriver();
        try {
            resultMessage = dao.insertOneTrade(con, trade);
            if (resultMessage.contains(Message.ERROR.getMessage())){
                status.setMessage(resultMessage);
                return status;
            }
            System.out.println(resultMessage);
            status.setMessage(accountDao.updateOneAccount(con,trade,false));
        } finally {
            if(con!=null) con.close();
        }
        status.setPageTag(Tag.MAIN);

        return status;
    }

    public Status transfer(Status status) throws SQLException {
        Connection con = null;
        ArrayList<Account> requestList = new ArrayList<>();
        ArrayList<Account> targetList = new ArrayList<>();
        Trade trade = (Trade) status.getDataValue(Tag.NEW_TRADE.getTag());
        String resultMessage;

        // 신규 송금 Trade객체 유효성 검증ㅇ
        con = driverConnector.connectDriver();
        try {
            requestList = accountDao.selectOne(con, trade.getRequestAccount());
            targetList = accountDao.selectOne(con, trade.getTargetAccount());
        } finally {
            if(con!=null) con.close();
        }

        if (targetList.isEmpty()) {
            status.setMessage(Message.ERROR_WRONG_REQUEST_ACCOUNT.getMessage());
            return status;
        } else if (requestList.isEmpty()) {
            status.setMessage(Message.ERROR_WRONG_TARGET_ACCOUNT.getMessage());
            return status;
        }

        int newReqBalance = requestList.getFirst().getBalance() - trade.getAmount();
        int newTarBalance = targetList.getFirst().getBalance() + trade.getAmount();

        if (newReqBalance < 0) {
            status.setMessage(Message.ERROR_FAILED_TRANSFER_OVER_BALANCE.getMessage(Tag.ACTION_TRANSFER.getTag()));
            return status;
        }
        trade.setTarBalance(newTarBalance);
        trade.setReqBalance(newReqBalance);

        // 신규 송금 Trade insert 후 각 account에 송금결과 update
        con = driverConnector.connectDriver();
        try {
            status.setMessage(dao.insertOneTrade(con, trade));
            // 요청 계좌 업데이트
            status.setMessage(accountDao.updateOneAccount(con,trade,true));
            // 수령 계좌 업데이트
            status.setMessage(accountDao.updateOneAccount(con,trade,false));
            status.setPageTag(Tag.MANAGE_ACCOUNTS);
        } finally {
            if(con!=null) con.close();
        }
        return status;
    }

    public Status selectAccountTradeHistory(Status status) throws SQLException {
        Connection con = null;
        ArrayList<Trade> tradeList;

        con = driverConnector.connectDriver();
        try{
            String accountNum = (String) status.getDataValue(Tag.ACCOUNT_NUMBER.getTag());
            tradeList = dao.selectAccountTradeHistory(con, status.getUserId(), accountNum); // accountNumber 주입
        } finally {
            if(con!=null) con.close();
        }

        status.setDataValue(Tag.PUT_DATA,Tag.RESULT_TRADE_LIST.getTag(),tradeList);

        return status;
    }

}

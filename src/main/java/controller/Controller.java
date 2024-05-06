package controller;

import domain.account.Account;
import domain.account.AccountService;
import view.page.*;
import view.Status;
import domain.trade.Trade;
import domain.trade.TradeService;
import domain.user.User;
import domain.user.UserService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {

    public Status serviceMenu (Status status){

        switch (status.getPageTag()) {
            case LOG_IN, SIGN_UP, MY_PAGE -> userService(status);
            case MANAGE_ACCOUNTS, OPEN_ACCOUNT, CLOSE_ACCOUNT -> accountService(status);
            case ACCOUNT_HISTORY, DEPOSIT, WITHDRAW, TRANSFER -> tradeService(status);
            default -> throw new IllegalStateException(Message.ERROR_WRONG_INPUT.getMessage());
        }
        return status;
    }


    public Status userService (Status status) {
        UserService service = new UserService();
        User user = new User();
        boolean inputError = true;

        switch (status.getPageTag()){
            case LOG_IN -> {
            if (status.getUserId().equals(Tag.NEW_GUEST.getTag())){
                    status = UserPage.checkUserPage(status);
                }
                if (status.getUserId().equals(Tag.OLD_GUEST.getTag())) {
                    status = UserPage.loginPage(status);
                    try {
                        status = service.logIn(status);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            case SIGN_UP -> {
                do {
                    try {
                        status = UserPage.signUpPage(status);
                        status = service.signUp(status);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (status.getMessage().contains(Message.INFO.getMessage())) {
                        inputError = false;
                    } else if (status.getMessage().contains(Message.ERROR.getMessage())) {
                        CommonPage.printMessage(status);
                    }
                } while (inputError);
            }
            case MY_PAGE -> {
                try {
                     status = service.myPage(status);
                     status = UserPage.myInfoPage(status);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        CommonPage.printMessage(status);
        CommonPage.endOfPage(status);

        return status;
    }


    public Status accountService(Status status) {
        AccountService service = new AccountService();

        switch (status.getPageTag()) {
            case MANAGE_ACCOUNTS -> {
                ArrayList<Account> accountList;
                try {
                     status = service.selectMyAccounts(status);
                     status = AccountPage.manageAccountPage(status);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            case OPEN_ACCOUNT -> {
                try {
                    status = AccountPage.openAccountPage(status);
                    status = service.insertOneAccount(status);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            case CLOSE_ACCOUNT -> {
                try {
                    status = service.deleteClosedAccount(status);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        CommonPage.printMessage(status);
        CommonPage.endOfPage(status);

        return status;
    }


    public Status tradeService(Status status) {
        Connection con = null;
        TradeService service = new TradeService();
        Trade trade;

        switch (status.getPageTag()) {

            case ACCOUNT_HISTORY -> {
                ArrayList<Trade> tradeList;
                try {
                    status = service.selectAccountTradeHistory(status);
                    status = AccountPage.accountHistoryPage(status);

                } catch (SQLException e) {

                }
            }
            case DEPOSIT, WITHDRAW -> {
                AccountService accountService = new AccountService();
                ArrayList<Account> accountList = new ArrayList<>();

                System.out.println(status.toString());
                if (status.getPageTag().equals(Tag.DEPOSIT) && !status.getData().equals(Tag.MY_ACCOUNT.getTag())) {
                    status = TradePage.checkDepoistToOtherPage(status);
                }

                if (status.getPageTag().equals(Tag.WITHDRAW) ||
                    (status.getPageTag().equals(Tag.DEPOSIT) && status.getData().equals(Tag.MY_ACCOUNT.getTag()))){
                    try {
                        status = accountService.selectMyAccounts(status);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    status = TradePage.selectAccountPage(status, accountList);
                }

                if (!status.getMessage().contains(Message.ERROR.getMessage())
                    && !status.getMessage().contains(Message.CANCLE.getMessage())) {
                    // 입/출금 정보 생성
                    trade = TradePage.inputTradeInfo(status);
                    // 거래 insert 후 계좌 update 실행
                    try {
                        status = service.insert(status);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            case TRANSFER -> {
                AccountService accountService = new AccountService();
                ArrayList<Account> accountList = new ArrayList<>();
                boolean inputError = true;

                try {
                    status = accountService.selectMyAccounts(status);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                status = TradePage.selectAccountPage(status, accountList);

                if (!status.getMessage().contains(Message.ERROR.getMessage())
                    && !status.getMessage().contains(Message.CANCLE.getMessage())) {
                    // 송금정보 생성
                    trade = TradePage.inputTradeInfo(status);

                    do {
                        // 보내는/받는 계좌 및 송금액 입력
                        try {
                            status = service.transfer(status);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        inputError = false;
                    } while (inputError == true);
                }
            }
        }
        CommonPage.printMessage(status);
        CommonPage.endOfPage(status);

        return status;
    }
}

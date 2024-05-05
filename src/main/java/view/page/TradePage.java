package view.page;

import domain.account.Account;
import view.ScannerSingleton;
import view.Status;
import common.CommonMethod;
import controller.Message;
import controller.Tag;
import domain.trade.Trade;

import java.util.ArrayList;
import java.util.Scanner;

public class TradePage {
    static Scanner sc = ScannerSingleton.getInstance().getScanner();

    public static Status checkDepoistToOtherPage(Status status) {
        String page;

        page = "----------------------------------\n";
        page += "           입금 대상 선택\n";
        page += "----------------------------------\n";
        page += "  1. 본인 계좌\n";
        page += "  2. 타인 계좌\n";
        page += "----------------------------------\n";
        page += "[input] ";
        System.out.print(page);
        String inputValue = sc.next();

        if (inputValue.equals("1")) {
            status.setDataValue(Tag.PUT_DATA, Tag.MY_ACCOUNT.getTag(),Tag.MY_ACCOUNT);
        } else if (inputValue.equals("2")) {
            status.setDataValue(Tag.PUT_DATA, Tag.OTHER_ACCOUNT.getTag(), Tag.OTHER_ACCOUNT);
        } else {
            status.setMessage(Message.ERROR_WRONG_INPUT_WORKNUMBER.getMessage());
        }

        return status;
    }


    public static Status selectAccountPage(Status status, ArrayList<Account> accountList) {
        String page, inputValue, action;
        action = "";

        if (status.getPageTag().equals(Tag.DEPOSIT)) {
            action = Tag.ACTION_DEPOSIT.getTag();
        } else if (status.getPageTag().equals(Tag.WITHDRAW)) {
            action = Tag.ACTION_WITHDRAW.getTag();
        } else if (status.getPageTag().equals(Tag.TRANSFER)) {
            action = Tag.ACTION_TRANSFER.getTag();
        }

        page = "----------------------------------\n";
        page += "        " + action + "할 내 계좌 선택 \n";
        page += "----------------------------------\n";
        if (accountList.isEmpty()) {
            page += " 보유한 계좌가 없습니다.\n";
        } else {
            page += "No | 상품 |   계좌번호   | 잔액     \n";
            page += "----------------------------------\n";

            Account myAccount = new Account();
            for (int i = 0; i < accountList.size(); i++) {
                myAccount = accountList.get(i);
                if ( i < 9 ) {
                    page += " " + (i+1) + " | ";
                } else {
                    page += (i+1) + " | ";
                }
                page += myAccount.getProductType() + " | ";
                page += myAccount.getaccountNum() + " | ";
                page += myAccount.getBalance() + "\n";
            }
        }
        page += "----------------------------------\n";

        if (!accountList.isEmpty()) {
            page += "     " + action+ "할 계좌의 번호(No)를 선택하세요.\n";
            page += "  0. 뒤로가기\n";
            page += "----------------------------------\n";
            page += "[input] ";
            System.out.print(page);
            inputValue = sc.next();

            if (inputValue.equals("0")) {
                status.setPageTag(Tag.MAIN);
                status.setMessage(Message.INFO_CANCLE_TRADE.getMessage(action));
                return status;
            } else if (CommonMethod.matchByRegex(Tag.REGEX_ONLY_NUMBER.getTag(), inputValue)) {
                status.setDataValue(Tag.PUT_DATA,Tag.MY_ACCOUNT.getTag(),
                                    accountList.get(Integer.parseInt(inputValue)-1).getaccountNum());
            }
        }
        if (accountList.isEmpty()) {
            page += " 계좌를 개설하시겠습니까?.\n";
            page += "  1. 예\n";
            page += "  2. 아니오\n";
            page += "----------------------------------\n";
            page += "[input] ";
            System.out.print(page);
            inputValue = sc.next();
            if (inputValue.equals("1")) {
                status.setPageTag(Tag.OPEN_ACCOUNT);
            } else if (inputValue.equals("2")) {
                status.setMessage(Message.INFO_CANCLE_OPEN_ACCOUNT.getMessage());
                status.setPageTag(Tag.MAIN);
            } else {
                status.setMessage(Message.ERROR_WRONG_INPUT_WORKNUMBER.getMessage());
            }
        }
        System.out.println("----------------------------------");

        return status;
    }

    public static Trade inputTradeInfo(Status status) {
        Trade trade = new Trade();

        switch (status.getPageTag()) {
            case DEPOSIT -> trade.setAction(Tag.ACTION_DEPOSIT);
            case WITHDRAW -> trade.setAction(Tag.ACTION_WITHDRAW);
            case TRANSFER -> trade.setAction(Tag.ACTION_TRANSFER);
        }

        System.out.println("----------------------------------");
        System.out.println("          "+ trade.getAction() +" 정보 입력");
        System.out.println("----------------------------------");

        switch (trade.getAction()) {
            case ACTION_DEPOSIT, ACTION_WITHDRAW  -> {
                trade.setRequestAccount(Tag.DEFAULT_ACCOUNT.getTag());
                if (trade.getAction().equals(Tag.ACTION_DEPOSIT) && status.getData().equals(Tag.OTHER_ACCOUNT)) {
                    System.out.print("입금 계좌: ");
                    trade.setTargetAccount(sc.next());
                } else {
                    String depositAccount = (String) status.getDataValue(Tag.ACCOUNT_NUMBER.getTag());
                    trade.setTargetAccount(depositAccount);
                }
            }
            case ACTION_TRANSFER -> {
                String requestAccount = (String) status.getDataValue(Tag.MY_ACCOUNT.getTag());
                trade.setRequestAccount(requestAccount);
                System.out.print("받는 계좌: ");
                trade.setTargetAccount(sc.next());
            }
        }
        System.out.print(trade.getAction() + " 금액: ");
        trade.setAmount(sc.nextInt());
        System.out.println("----------------------------------");

        return trade;
    }
}

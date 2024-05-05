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

public class AccountPage {
    static Scanner sc = ScannerSingleton.getInstance().getScanner();

    public static Status manageAccountPage(Status status, ArrayList<Account> accountList) {
        String page, inputValue, account_owner;

        page = "----------------------------------\n";
        page += "        나의 계좌 내역 (" + accountList.size() +")\n";
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
        page += " 수행할 작업을 입력하세요.\n";
        if (accountList.isEmpty()){
            page += "  1. 계좌 개설\n";
        } else if (!accountList.isEmpty()) {
            page += "  1. 입금\n";
            page += "  2. 출금\n";
            page += "  3. 송금\n";
            page += "  4. 거래내역 조회\n";
            page += "  5. 계좌 개설\n";
            page += "  6. 계좌 해지\n";
        }
        page += "  0. 뒤로가기\n";
        page += "----------------------------------\n";
        page += "[input] ";
        System.out.print(page);
        inputValue = sc.next();
        if (inputValue.equals("0")) {
            status.setPageTag(Tag.MAIN);
        } else if (accountList.isEmpty() && inputValue.equals("1")
                || !accountList.isEmpty() && inputValue.equals("5")) {
            status.setPageTag(Tag.OPEN_ACCOUNT);
        } else if (!accountList.isEmpty() && (inputValue.equals("1") || inputValue.equals("2") || inputValue.equals("3"))) {
            String action = "";
            switch (inputValue){
                case "1" -> { status.setPageTag(Tag.DEPOSIT);
                    status.setDataValue(Tag.PUT_DATA,Tag.MY_ACCOUNT.getTag(), Tag.MY_ACCOUNT);
                    action = Tag.ACTION_DEPOSIT.getTag();
                }
                case "2" -> { status.setPageTag(Tag.WITHDRAW);
                    action = Tag.ACTION_WITHDRAW.getTag();

                }
                case "3" -> { status.setPageTag(Tag.TRANSFER);
                    action = Tag.ACTION_TRANSFER.getTag();
                }
            }
            page += "     " + action + "할 계좌의 번호(No)를 선택하세요.\n";
            page += "  0. 뒤로가기\n";
            page += "----------------------------------\n";
            page += "[input] ";
            System.out.print(page);
            inputValue = sc.next();
            if (inputValue.equals("0")) {
                status.setPageTag(Tag.MANAGE_ACCOUNTS);
                status.setMessage(Message.INFO_CANCLE_TRADE.getMessage(action));
                return status;
            } else if (CommonMethod.matchByRegex(Tag.REGEX_ONLY_NUMBER.getTag(), inputValue)) {
                status.setDataValue(Tag.PUT_DATA, Tag.MY_ACCOUNT.getTag(),
                        accountList.get(Integer.parseInt(inputValue)-1).getaccountNum());
            }
        } else if (!accountList.isEmpty() && inputValue.equals("4")) {
            System.out.println("----------------------------------");
            System.out.println(" 조회할 계좌의 번호(No)를 선택 하세요.");
            System.out.println("----------------------------------");
            System.out.print("[input] ");
            status.setDataValue(Tag.PUT_DATA,Tag.MY_ACCOUNT.getTag(),
                    accountList.get(Integer.parseInt(inputValue)-1).getaccountNum());
            status.setPageTag(Tag.ACCOUNT_HISTORY);
        } else if (!accountList.isEmpty() && inputValue.equals("6")) {
            System.out.println("----------------------------------");
            System.out.println(" 해지할 계좌의 번호(No)를 선택 하세요.");
            System.out.println("----------------------------------");
            System.out.print("[input] ");
            status.setDataValue(Tag.PUT_DATA, Tag.MY_ACCOUNT.getTag(),
                    accountList.get(Integer.parseInt(inputValue)-1).getaccountNum());
            status.setPageTag(Tag.CLOSE_ACCOUNT);
        } else {
            status.setMessage(Message.ERROR_WRONG_INPUT_WORKNUMBER.getMessage());
        }
        System.out.println("----------------------------------");

        return status;
    }

    public static Status accountHistoryPage(Status status, ArrayList<Trade> tradeList) {
        String page;

        page = "----------------------------------\n";
        page += "     "+ status.getData() + " 계좌 거래내역\n";
        page += "----------------------------------\n";
        if (tradeList.isEmpty()) {
            page += "거래내역이 없습니다.\n";
        } else {
            page += "No | 거래 | 거래 계좌번호 | 거래액     \n";
            page += "----------------------------------\n";
            // 거래 내역 출력
            String accountNum;
            for (Trade trade : tradeList) {
                if (trade.getRequestAccount().equals("-")) {
                    accountNum = " 본인 거래 ";
                } else {
                    accountNum = trade.getTargetAccount();
                }
                if ( trade.getTradeId() < 10 ) {
                    page += " " + trade.getTradeId() + " | ";
                } else {
                    page += trade.getTradeId() + " | ";
                }
                page += trade.getAction() + " | ";
                page += accountNum + " | ";
                page += trade.getAmount() + "\n";
            }
        }
        page += "----------------------------------\n";
        System.out.println(page);
        status.setPageTag(Tag.MANAGE_ACCOUNTS);
        status.setWorkFlow(Tag.STAY_FOR_INPUT);

        return status;
    }


    public static Status closeAccountPage(Status status) {
        String page, inputValue;

        page = "----------------------------------\n";
        page += " 정말 " + status.getData() + "계좌 삭제를 진행합니까?\n";
        page += "----------------------------------\n";
        page += "  1. 예\n";
        page += "  0. 취소\n";
        page += "----------------------------------\n";
        System.out.println(page);
        inputValue = sc.next();
        if (inputValue.equals("0")) {
            status.setMessage(Message.INFO_CANCLE_CLOSE_ACCOUNT.getMessage());
            status.setPageTag(Tag.MANAGE_ACCOUNTS);
        } else {
            status.setMessage(Message.ERROR_WRONG_INPUT_WORKNUMBER.getMessage());
        }

        return status;
    }


    public static Status openAccountPage(Status status) {
        String page, inputValue;

        page = "----------------------------------\n";
        page += "           신규 계좌 개설\n";
        page += "----------------------------------\n";
        page += "가입할 상품의 종류를 입력하세요.\n";
        page += "  1. 예금\n";
        page += "  2. 적금\n";
        page += "  3. CMA\n";
        page += "  0. 취소\n";
        page += "----------------------------------\n";
        System.out.println(page);

        inputValue = sc.next();
        if (inputValue.equals("0")) {
            status.setMessage(Message.INFO_CANCLE_OPEN_ACCOUNT.getMessage());
            status.setPageTag(Tag.MANAGE_ACCOUNTS);
        } else if (inputValue.equals("1")) {
            status.setDataValue(Tag.PUT_DATA, Tag.DEPOSIT_ACCOUNT.getTag(),Tag.DEPOSIT_ACCOUNT);
        } else if (inputValue.equals("2")) {
            status.setDataValue(Tag.PUT_DATA, Tag.SAVINGS_ACCOUNT.getTag(), Tag.SAVINGS_ACCOUNT);
        } else if (inputValue.equals("3")) {
            status.setDataValue(Tag.PUT_DATA, Tag.CACHE_MANAGEMENT_ACCOUNT.getTag(), Tag.CACHE_MANAGEMENT_ACCOUNT);
        } else {
            status.setMessage(Message.ERROR_WRONG_INPUT_WORKNUMBER.getMessage());
        }
        System.out.println(Message.INFO_EXCUTE_OPEN_ACCOUNT.getMessage());

        return status;
    }
}

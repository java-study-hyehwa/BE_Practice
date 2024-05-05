package view.page;

import view.ScannerSingleton;
import view.Status;
import controller.Message;
import controller.Tag;
import java.util.Scanner;

public class MainPage {
    static Scanner sc = ScannerSingleton.getInstance().getScanner();

    public static Status mainPage(Status status) {
        String page, inputValue;

        page = "----------------------------------\n";
        page += "             뱅킹 시스템            \n";
        page += "----------------------------------\n";
        page += "  1. 내 정보\n";
        page += "  2. 계좌 관리\n";
        page += "  3. 입금\n";
        page += "  4. 출금\n";
        page += "  5. 송금\n";
        page += "  0. 사용 종료\n";
        page += "----------------------------------\n";
        page += " [input] ";
        System.out.print(page);
        inputValue = sc.next();
        if (inputValue.equals("1")) {
            status.setPageTag(Tag.MY_PAGE);
        } else if (inputValue.equals("2")) {
            status.setPageTag(Tag.MANAGE_ACCOUNTS);
        } else if (inputValue.equals("3")) {
            status.setPageTag(Tag.DEPOSIT);
        } else if (inputValue.equals("4")) {
            status.setPageTag(Tag.WITHDRAW);
        } else if (inputValue.equals("5")) {
            status.setPageTag(Tag.TRANSFER);
        } else {
            status.setMessage(Message.ERROR_WRONG_INPUT_WORKNUMBER.getMessage());
        }
        return status;
    }
}

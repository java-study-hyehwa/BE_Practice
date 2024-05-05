package view.page;

import java.util.Scanner;

import view.ScannerSingleton;
import view.Status;
import controller.*;

public class CommonPage {
    static Scanner sc = ScannerSingleton.getInstance().getScanner();

    public static void printMessage (Status status) {
        if (!status.getMessage().equals(Tag.DEFAULT_DATA.getTag())) {
            System.out.println(status.getMessage());
            status.setMessage(Tag.DEFAULT_DATA.getTag());
        }
    }

    public static void endOfPage (Status status) {
        String page;

        if (!status.getRun()) {
            page = "----------------------------------\n";
            page += "       뱅킹시스템 사용 종료\n";
            page += "----------------------------------\n";
            System.out.println(page);
        }

        if (status.getWorkFlow().equals(Tag.STAY_FOR_INPUT)){
            page = "[Info] 아무 키나 입력하여 계속: ";
            System.out.print(page);
            String a = sc.next();
        }
    }

}

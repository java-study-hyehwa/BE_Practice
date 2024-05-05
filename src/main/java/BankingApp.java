import view.Status;
import controller.*;
import view.page.MainPage;

public class BankingApp {

    public static void main(String[] args) {

        Controller controller = new Controller();
        Status status = new Status();

        do {
            if (status.getPageTag().equals(Tag.MAIN)
                && !status.getUserId().equals(Tag.NEW_GUEST.getTag())) {
                status = MainPage.mainPage(status);
            }
            controller.serviceMenu(status);

        } while (status.getRun());

    }


}

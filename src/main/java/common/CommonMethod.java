package common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CommonMethod {

    public static boolean matchByRegex(String regex, String inputValue) {
        return inputValue.matches(regex);
    }


    public static LocalDate parseStrToLocalDate(String strDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate result = LocalDate.parse(strDate,formatter);

        return result;

    }
}

package view.page;

import view.ScannerSingleton;
import view.Status;
import common.CommonMethod;
import controller.Message;
import controller.Tag;
import domain.user.User;


import java.util.Scanner;
import java.util.Objects;

public class UserPage {
    static Scanner sc = ScannerSingleton.getInstance().getScanner();

    public static Status checkUserPage(Status status) {
        String inputValue, page;

        page = "----------------------------------\n";
        page += "              회원 확인\n";
        page += "----------------------------------\n";
        page += " 회원가입 여부를 선택해주세요.\n";
        page += "  1. 회원 고객\n";
        page += "  2. 비회원 고객\n";
        page += "  0. 사용종료\n";
        page += "----------------------------------\n";
        page += "[input] ";
        System.out.print(page);
        inputValue = sc.nextLine().replace("\n","");
        if (inputValue.equals("0")) {
            status.setRun(false);
        } else if (inputValue.equals("1")) {
            status.setMessage(Message.INFO_EXCUTE_LOGIN.getMessage());
            status.setUserId(Tag.OLD_GUEST.getTag());
        } else if (inputValue.equals("2")){
            status.setMessage(Message.INFO_EXCUTE_SIGNUP.getMessage());
            status.setPageTag(Tag.SIGN_UP);
        } else {
            status.setMessage(Message.ERROR_WRONG_INPUT_WORKNUMBER.getMessage());
        }

        return status;
    }


    public static Status loginPage(Status status) {
        User user = new User();
        String page;

        page = "----------------------------------\n";
        page += "              log-in\n";
        page += "----------------------------------\n";
        page += "- ID: ";
        System.out.print(page);
        user.setUserId(sc.nextLine().replace("\n",""));
        System.out.print("- PW: ");
        user.setPassword(sc.nextLine().replace("\n",""));
        System.out.println("----------------------------------");

        status.setDataValue(Tag.PUT_DATA,Tag.TRY_LOGIN_USER.getTag(),user);

        return status;
    }


    public static Status signUpPage(Status status) {
        User user = new User();
        String page, inputValue;
        boolean correctInput = false;
        String [] inputList = {"id", "password", "이름", "생년월일", "성별", "주소", "휴대폰 번호", "직업", "연봉"};

        page = "----------------------------------\n";
        page += "         신규 가입 정보 입력\n";
        page += "----------------------------------\n";
        System.out.println(page);

        for (int locationNum = 0; locationNum < inputList.length; locationNum++) {
            switch (inputList[locationNum]) {
                case "생년월일" -> System.out.println("[예시] 2000-01-01");
                case "성별" -> System.out.println("[선택] 1. 남성 / 2. 여성");
                case "휴대폰 번호" -> System.out.println("[예시] 010-1111-2222");
            }
            System.out.print(inputList[locationNum] + ": ");
            inputValue = sc.nextLine().replace("\n","");
            user = setInputNewUser(inputList[locationNum], inputValue, user);

            switch (inputList[locationNum]) {
                case "id": correctInput = !user.getUserId().equals(Tag.NEW_GUEST.getTag());
                case "password": correctInput = Objects.nonNull(user.getPassword());
                case "이름": correctInput = Objects.nonNull(user.getName());
                case "생년월일": correctInput = Objects.nonNull(user.getBirth_date());
                case "성별": correctInput = Objects.nonNull(user.getGender());
                case "주소": correctInput = Objects.nonNull(user.getAddress());
                case "휴대폰 번호": correctInput = Objects.nonNull(user.getPhoneNumber());
                case "직업": correctInput = Objects.nonNull(user.getJob());
                case "연봉": correctInput = Objects.nonNull(user.getAnnualSalary());
            }

            if (!correctInput) {
                status.setMessage(Message.ERROR_WRONG_INPUT_SIGNUP.getMessage(inputList[locationNum]));
                System.out.println("----------------------------------");
                return status;
            };

        }

        System.out.println("----------------------------------");

        System.out.println("new_user: " + user.toString());// 테스트용 메시지

        status.setDataValue(Tag.PUT_DATA, Tag.NEW_USER.getTag(), user);


        return status;
    }

    public static Status myInfoPage(Status status) {
        String page;
        String gender = "";
        User user = (User) status.getDataValue(Tag.RESULT_USER.getTag());

        switch (user.getGender()) {
            case 'M' -> gender = "남성";
            case 'F' -> gender = "여성";
        }

        page = "----------------------------------\n";
        page += "              내 정보\n";
        page += "----------------------------------\n";
        page += " ID: " + status.getUserId() + "\n";
        page += " 이름: " + user.getName() + "\n";
        page += " 생년월일: " + user.getBirth_date() + "\n";
        page += " 성별: " + gender + "\n";
        page += " 휴대폰 번호: " + user.getPhoneNumber() + "\n";
        page += " 주소: " + user.getAddress() + "\n";
        page += " 가입날짜: " + user.getSignUpDate() + "\n";
        page += "----------------------------------\n";
        System.out.println(page);
        status.setPageTag(Tag.MAIN);
        status.setWorkFlow(Tag.STAY_FOR_INPUT);

        return status;
    }

    public static User setInputNewUser(String location, String inputValue, User user) {
        switch (location) {
            case "id" -> {
                if (!CommonMethod.matchByRegex(Tag.REGEX_ONLY_NUMBER.getTag(),inputValue)) {
                    user.setUserId(inputValue);
                }
            }
            case "password" -> {
                if (CommonMethod.matchByRegex(Tag.REGEX_ONLY_NUMBER.getTag(),inputValue)) {
                    user.setPassword(inputValue);
                }
            }
            case "이름" -> {
                if (!CommonMethod.matchByRegex(Tag.REGEX_ONLY_NUMBER.getTag(),inputValue)) {
                    user.setName(inputValue);
                }
            }
            case "생년월일" -> {
                if (CommonMethod.matchByRegex(Tag.REGEX_LOCAL_DATE.getTag(),inputValue)) {
                    user.setBirth_date(CommonMethod.parseStrToLocalDate(inputValue));
                }
            }
            case "성별" -> {
                if (CommonMethod.matchByRegex(Tag.REGEX_ONLY_NUMBER.getTag(),inputValue)) {
                    switch (inputValue) {
                        case "1" -> user.setGender('M');
                        case "2" -> user.setGender('F');
                        default -> user.setGender(null);
                    }
                }
            }
            case "휴대폰 번호" -> {
                if (CommonMethod.matchByRegex(Tag.REGEX_PHONE_NUMBER.getTag(),inputValue)) {
                    user.setPhoneNumber(inputValue);
                }
            }
            case "주소" -> {
                if (!CommonMethod.matchByRegex(Tag.REGEX_ONLY_NUMBER.getTag(),inputValue)) {
                    user.setAddress(inputValue);
                }
            }
            case "직업" -> {
                if (!CommonMethod.matchByRegex(Tag.REGEX_ONLY_NUMBER.getTag(),inputValue)) {
                    user.setJob(inputValue);
                }
            }
            case "연봉" -> {
                if (CommonMethod.matchByRegex(Tag.REGEX_ONLY_NUMBER.getTag(),inputValue)) {
                    user.setAnnualSalary(Integer.parseInt(inputValue));
                }
            }
        }

        return user;
    }
}

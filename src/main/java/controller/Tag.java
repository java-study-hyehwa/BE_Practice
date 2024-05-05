package controller;

import java.util.function.Supplier;

public enum Tag {
    // Tags to controll changing page
    MAIN(() -> "main"),
    LOG_IN(() -> "log_in"),
    SIGN_UP(() -> "sign_up"),
    MY_PAGE(() -> "my_page"),
    MANAGE_ACCOUNTS(() -> "manage_accounts"),
    OPEN_ACCOUNT(() -> "open_account"),
    CLOSE_ACCOUNT(() -> "close_account"),
    ACCOUNT_HISTORY(() -> "account_history"),
    DEPOSIT(() -> "deposit"),
    WITHDRAW(() -> "withdraw"),
    TRANSFER(() -> "transfer"),

    // Tags for controlling workflow
    CONTINUE(() -> "continue"),
    STAY_FOR_INPUT(() -> "stay_for_input"),
    RETRY(() -> "retry"),
    GUEST(() -> "guest"),
    NEW_GUEST(() -> "non_signed_guest"),
    OLD_GUEST(() -> "signed_guest"),

    // Tags for Trade.action field
    DEFAULT_ACCOUNT(() -> "0000000000"),
    ACTION_DEPOSIT(() -> "입금"),
    ACTION_WITHDRAW(() -> "출금"),
    ACTION_TRANSFER(() -> "송금"),

    // Tags for Status Object
    DEFAULT_DATA (() -> ""),
    PUT_DATA(() -> "put_data"),
    TRY_LOGIN_USER(() -> "try_login_user"),
    RESULT_USER(() -> "result_user"),
    RESULT_USER_LIST(() -> "user_list"),
    ACCOUNT_OWNER(() -> "account_owner"),
    ACCOUNT_NUMBER(() -> "account_number"),
    ACCOUNT_TYPE(() -> "account_type"),
    UPDATE_DATA(() -> "update_data"),
    MY_ACCOUNT(() -> "my_account"),
    OTHER_ACCOUNT(() -> "other_account"),
    DEPOSIT_ACCOUNT(() -> "예금"),
    SAVINGS_ACCOUNT(() -> "적금"),
    CACHE_MANAGEMENT_ACCOUNT(() -> "CMA"),

    // Tags for Regex
    REGEX_ONLY_NUMBER(() -> "[0-9]+"),
    REGEX_LOCAL_DATE(() -> "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])"),
    REGEX_PHONE_NUMBER(() -> "^\\d{3}-\\d{3,4}-\\d{4}$");


    private final Supplier<String> tag;

    Tag(Supplier<String> tag) {
        this.tag = tag;
    }

    Tag(String tag){
        this.tag = Tag.valueOf(tag).tag;
    }

    public String getTag(){
        return tag.get();
    }

}

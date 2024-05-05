package common.repository;

import java.util.function.Supplier;


public enum Query {
    //UserDao
    USER_SELECT_LOGIN("log_in", () -> "select user_id, name, sign_up_date from Users where user_id = ? and password = ?;"),
    USERL_SELECT_CHECK_EXIST("check_exist_user", () -> "select user_id from Users where user_id = ?);"),
    USER_SELECT_MY_INFO("my_page", () -> "select birth_date, gender, address, job from Users where user_id = ?;"),
    USER_INSERT_SIGNUP("sign_up", () -> "insert into Users values(nextval('user_seq'),?,?,?,?,?,?,?,?);"),
    USER_DELETE_WITHDRAWAL("withdrawal_user", () -> "delete from Users where user_id = ?"),

    //AccountDao
    ACCOUNT_SELECT_MY_ALL("manage_accounts", () -> "select account_number, Users_user_id, product_type, balance " +
                                                          "from Accounts where Users_user_id = ?;"),
    ACCOUNT_SELECT_ONE("select_account", () -> "select account_number, Users_user_id,product_type, balance  \n" +
                                                "from practice_db.Accounts \n" +
                                                "where account_number = ?;"),
    ACCOUNT_INSERT_ONE("open_account", () -> "insert into Accounts values(nextval('account_seq'),?,?,?,?,?);"),
    ACCOUNT_DELETE_ONE("close_account", () -> "delete from Accounts where account_number = ?;"),
    ACCOUNT_UPDATE_ONE("trade", () -> "update Accounts set balance = ? where account_number = ?;"),

    //TradeDAO
    TRADE_INSERT_ONE("insert_trade", () -> "insert into Trades values(nextval('trade_seq'),?,?,?,?,?,?);"),
    TRADE_SELECT_ACCOUNT_HISTORY("trade_history", () -> "select trade_id, action, request_account, target_account, amount from Trades " +
                                                     "where request_account = ? or target_account = ? " +
                                                     "order by trade_id;");

    private final String queryName;
    private final Supplier<String> queryString;

    Query(String key, Supplier<String> queryString) {
        this.queryName = key;
        this.queryString = queryString;
    }


    public String getQueryString(){
        return queryString.get();
    }

}

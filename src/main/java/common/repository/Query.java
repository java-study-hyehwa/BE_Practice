package common.repository;

import java.util.function.Supplier;


public enum Query {
    //UserDao
    USER_SELECT_LOGIN(() -> "select user_id, name, sign_up_date from Users where user_id = ? and password = ?;"),
    USERL_SELECT_CHECK_EXIST(() -> "select user_id from Users where user_id = ?);"),
    USER_SELECT_MY_INFO(() -> "select name, birth_date, gender, address, phone_number, job, sign_up_date from Users where user_id = ?;"),
    USER_INSERT_SIGNUP(() -> "insert into Users values(nextval('user_seq'),?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP);"),
    USER_DELETE_WITHDRAWAL(() -> "delete from Users where user_id = ?"),

    //AccountDao
    ACCOUNT_SELECT_MY_ALL(() -> "select account_number, owner_id, product_type, balance " +
                                                          "from Accounts where owner_id = ?;"),
    ACCOUNT_SELECT_ONE(() -> "select account_number, owner_id,product_type, balance  \n" +
                                                    "from Accounts where account_number = ?;"),
    ACCOUNT_INSERT_ONE(() -> "insert into Accounts values(nextval('account_seq'),?,?,?,?,CURRENT_TIMESTAMP);"),
    ACCOUNT_DELETE_ONE(() -> "delete from Accounts where account_number = ?;"),
    ACCOUNT_UPDATE_ONE(() -> "update Accounts set balance = ? where account_number = ?;"),

    //TradeDAO
    TRADE_INSERT_ONE(() -> "insert into Trades values(nextval('trade_seq'),?,?,?,?,?,?,?,CURRENT_TIMESTAMP);"),
    TRADE_SELECT_ACCOUNT_HISTORY(() -> "select trade_id, action, request_account, target_account, amount from Trades " +
                                                     "where request_account = ? or target_account = ? " +
                                                     "order by trade_id;");


    private final Supplier<String> queryString;

    Query(Supplier<String> queryString) {
        this.queryString = queryString;
    }


    public String getQueryString(){
        return queryString.get();
    }

}

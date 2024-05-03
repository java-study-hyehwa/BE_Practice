package accountManager;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.lang.System.getenv;

public class AccountManager {
    static Map<String, String> env = getenv();
    private static final String URL = "jdbc:mysql://localhost:3306/javabank";
    private static final String USERNAME = env.get("DB_USER");
    private static final String PASSWORD = env.get("DB_PASSWORD");

    // 계좌 목록 조회 기능
    public Map<String, Double> showAccounts(String id) throws SQLException {

        Map<String, Double> accountLists = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT account_number, balance FROM Accounts WHERE user_id = ?")) {
            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String accountNumber = resultSet.getString("account_number");
                    double balance = resultSet.getDouble("balance");
                    accountLists.put(accountNumber, balance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountLists; // 계좌 목록 반환
    }

    // 잔액 조회 기능
    public double checkBalance(Connection connection, String accountNumber) {
        try (PreparedStatement statement = connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ?")) {
            statement.setString(1, accountNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("balance");
                } else {
                    System.out.println("해당 계좌가 존재하지 않습니다.");
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.getStackTrace();
            return -1; // 오류 반한
        }
    }

    // 계좌 조회 기능
    public void checkAccountDetails(String accountNumber) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Transactions WHERE deposit_account = ? OR withdrawal_account = ? ORDER BY transaction_date DESC LIMIT 5")) {
            statement.setString(1, accountNumber);
            statement.setString(2, accountNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println("최근 거래 내역:");
                System.out.println("거래날짜\t\t\t\t거래종류\t\t\t거래금액\t\t\t잔액");
                while (resultSet.next()) {
                    String transactionType = resultSet.getString("transaction_type");
                    Timestamp transactionDate = resultSet.getTimestamp("transaction_date");
                    double amount = resultSet.getDouble("transaction_amount");
                    String withdrawalAccount = resultSet.getString("withdrawal_account");
                    String depositAccount = resultSet.getString("deposit_account");
                    String transactionAccount = "";
                    if (withdrawalAccount != null && depositAccount != null) {
                        transactionAccount = withdrawalAccount.equals(accountNumber) ? depositAccount : withdrawalAccount;
                        // 이하 출력 코드
                    }
//                    double balance = resultSet.getDouble("balance_after_transaction");

                    // 날짜 형식 변환
                    String formattedDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transactionDate);

                    System.out.printf("%s\t%s\t\t%.2f\t\n", formattedDate, transactionType, amount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // 입금 기능
    public void deposit(String id, String accountNumber, double amount, String accountPassword) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement depositStatement = connection.prepareStatement("UPDATE Accounts SET balance = balance + ? WHERE account_number = ? AND account_password = ?");
             PreparedStatement transactionStatement = connection.prepareStatement("INSERT INTO Transactions (transaction_type, transaction_amount, transaction_date, deposit_account) VALUES (?, ?, NOW(), ?)")) {
            depositStatement.setDouble(1, amount);
            depositStatement.setString(2, accountNumber);
            depositStatement.setString(3, accountPassword);


            int rowsUpdated = depositStatement.executeUpdate();

            // Transaction 테이블에 거래 내역 저장
            transactionStatement.setString(1, "deposit");
            transactionStatement.setDouble(2, amount);
            transactionStatement.setString(3, accountNumber);

            int transactionRowsUpdated = transactionStatement.executeUpdate();

            if (rowsUpdated > 0 && transactionRowsUpdated > 0) {
                System.out.println("입금이 완료되었습니다.");
                double balance = checkBalance(connection, accountNumber);
                if (balance >= 0) {
                    System.out.printf("계좌 잔액: %.0f원 \n", balance);
                }

            } else {
                System.out.println("입금에 실패했습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 출금 기능
    public void withdrawal(String id, String accountNumber, double amount, String accountPassword) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement depositStatement = connection.prepareStatement("UPDATE Accounts SET balance = balance - ? WHERE account_number = ? AND account_password = ?");
             PreparedStatement transactionStatement = connection.prepareStatement("INSERT INTO Transactions (transaction_type, transaction_amount, transaction_date, withdrawal_account) VALUES (?, ?, NOW(), ?)")) {
            depositStatement.setDouble(1, amount);
            depositStatement.setString(2, accountNumber);
            depositStatement.setString(3, accountPassword);

            int rowsUpdated = depositStatement.executeUpdate();

            // Transaction 테이블에 거래 내역 저장
            transactionStatement.setString(1, "withdrawal");
            transactionStatement.setDouble(2, amount);
            transactionStatement.setString(3, accountNumber);

            int transactionRowsUpdated = transactionStatement.executeUpdate();

            if (rowsUpdated > 0 && transactionRowsUpdated > 0) {
                System.out.println("출금이 완료되었습니다.");
                double balance = checkBalance(connection, accountNumber);
                if (balance >= 0) {
                    System.out.printf("계좌 잔액: %.0f원 \n", balance);
                }

            } else {
                System.out.println("출금에 실패했습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 계좌 송금
    public void transfer(String withdrawalAccount, String depositAccount, double amount, String accountPassword) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false); // 트랜잭션 시작

            // 송금자 계좌에서 출금
            PreparedStatement withdrawStatement = connection.prepareStatement("UPDATE Accounts SET balance = balance - ? WHERE account_number = ? AND account_password = ?");
            withdrawStatement.setDouble(1, amount);
            withdrawStatement.setString(2, withdrawalAccount);
            withdrawStatement.setString(3, accountPassword);

            int withdrawRowsUpdated = withdrawStatement.executeUpdate();

            // 수신자 계좌로 입금
            PreparedStatement depositStatement = connection.prepareStatement("UPDATE Accounts SET balance = balance + ? WHERE account_number = ?");
            depositStatement.setDouble(1, amount);
            depositStatement.setString(2, depositAccount);
            int depositRowsUpdated = depositStatement.executeUpdate();

            // Transaction 테이블에 거래 내역 저장
            if (withdrawRowsUpdated > 0 && depositRowsUpdated > 0) {
                PreparedStatement transactionStatement = connection.prepareStatement("INSERT INTO Transactions (transaction_type, transaction_amount, transaction_date, deposit_account, withdrawal_account) VALUES (?, ?, NOW(), ?, ?)");
                transactionStatement.setString(1, "transfer");
                transactionStatement.setDouble(2, amount);
                transactionStatement.setString(3, depositAccount);
                transactionStatement.setString(4, withdrawalAccount);
                int transactionRowsUpdated = transactionStatement.executeUpdate();

                if (transactionRowsUpdated > 0) {
                    connection.commit(); // 트랜잭션 성공적으로 완료
                    System.out.println("송금이 완료되었습니다.");
                    double transferBalance = checkBalance(connection, withdrawalAccount);
                    if (transferBalance >= 0) {
                        System.out.printf("송금 후 %s 계좌 잔액: %.0f원\n", withdrawalAccount, transferBalance);
                    }
                } else {
                    throw new SQLException("거래 내역 저장에 실패했습니다.");
                }
            } else {
                throw new SQLException("송금에 실패했습니다.");
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback(); // 롤백
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // 트랜잭션 종료
                    connection.close();
                }
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }


    // 계좌 생성 기능
    public void createAccount(String userId, String accountPassword) {
        Random rand = new Random();
        long accountNumber = rand.nextLong(9000000000L) + 1000000000L;

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO accounts(account_number, user_id, account_password) VALUES (?, ?, ?)")) {
            statement.setString(1, String.valueOf(accountNumber));
            statement.setString(2, userId);
            statement.setString(3, accountPassword);


            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("계좌가 생성되었습니다.");
                System.out.println(userId + "님의 계좌번호는 " + accountNumber + "입니다.");
            } else {
                System.out.println("계좌 생성에 실패했습니다.");
            }

        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    // 계좌 삭제 기능
    public void deleteAccount(String accountNumber, String accountPassword) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM accounts WHERE account_number = ? AND account_password = ?")) {
            statement.setString(1, accountNumber);
            statement.setString(2, accountPassword);


            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("계좌가 삭제되었습니다.");
            } else {
                System.out.println("계좌 삭제에 실패했습니다.");
            }

        } catch (SQLException e) {
            e.getStackTrace();
        }

    }
}

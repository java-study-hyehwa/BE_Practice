package common.repository;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import common.SshConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverConnector {
    private static  String driver;
    private static String dbHost;
    private static int dbPort;
    private static String database;
    private static String userName;
    private static String password;
    private static int lPort;

    public DriverConnector() {
        this.driver = "com.mysql.cj.jdbc.Driver";
        this.dbHost = "bepractice-mysql-8033.cnyc406m4kh6.ap-northeast-2.rds.amazonaws.com";
        this.dbPort = 3306;
        this.database = "practice_db";
        this.userName = "admin";
        this.password = "Ucheol92!4";
        this.lPort = 36662;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUrl() {
        return dbHost;
    }

    public void setUrl(String url) {
        this.dbHost = dbHost;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Connection connectDriver() {
        Connection con = null;
        Session session = null;
        String rHost = "";
        int rPort;

        try {
            // create SSH Tuneling session
            SshConnector sshCondnector = new SshConnector();
            session = sshCondnector.getSession();
            rHost = session.getHost();

            System.out.println(rHost);

            // port fowarding to RDS MySQL
            int assinged_port = session.setPortForwardingL(lPort, dbHost, dbPort);
            System.out.println("localhost:"+assinged_port+" -> "+ dbHost +":"+dbPort);
        } catch (JSchException e) {
            e.printStackTrace();
        }


        if (session.isConnected()){
            try {
                // connect to RDS MySQL with JDBC
                Class.forName(driver);
                System.out.println("jdbc:mysql://" + rHost +":" + lPort + "/" + database);
                con = DriverManager.getConnection("jdbc:mysql://" + rHost +":" + lPort + "/" + database, userName, password);
            } catch (ClassNotFoundException e) {
                try {
                    session.noMoreSessionChannels();
                } catch (Exception f)  {
                    f.printStackTrace();
                }
                session.disconnect();
                e.printStackTrace();
            } catch (SQLException e) {
                session.disconnect();
                e.printStackTrace();
            }
        }


        return con;
    }
}

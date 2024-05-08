package common.repository;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import common.SshTunnel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class rdsDriverConnectorImpl implements DriverConnector {
    private static String driver;
    private static String dbHost;
    private static int dbPort;
    private static String database;
    private static String userName;
    private static String password;


    public void setConnectionInfo() {
        this.driver = "com.mysql.cj.jdbc.Driver";
        this.dbHost = "bepractice-mysql-8033.cnyc406m4kh6.ap-northeast-2.rds.amazonaws.com";
        this.dbPort = 3306;
        this.database = "practice_db";
        this.userName = "admin";
        this.password = "Ucheol92!4";
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

    @Override
    public Connection connectDriver() {
        Connection con = null;
        Session jumpSession = null;
        int assinged_port = 0;
        String osUser = System.getProperty("user.name");

        final String JUMP_USER = "ec2-user";
        final String  JUMP_HOST = "3.38.127.6";
        final int JUMP_PORT = 22;
        final String KNOWNHOSTS = "C:\\Users\\" + osUser + "\\.ssh\\known_hosts";
        final String JUMP_KEY = "C:\\Users\\" + osUser + "\\.ssh\\BEpractice-bastion-key.pem";

        setConnectionInfo();

        try {
            // create SSH Tuneling session
            SshTunnel jumpTunnel = SshTunnel.construct();
            jumpSession = jumpTunnel.createTunnel(JUMP_USER, JUMP_HOST,JUMP_PORT, KNOWNHOSTS, JUMP_KEY);

            // port fowarding to RDS MySQL
            assinged_port = jumpSession.setPortForwardingL(0, dbHost, dbPort);
            //System.out.println("Bastion EC2:"+assinged_port+" -> "+ dbHost +":"+dbPort)

        } catch (JSchException e) {
            e.printStackTrace();
        }

        if (jumpSession.isConnected()){
            try {
                // connect to RDS MySQL with JDBC
                Class.forName(driver);
                //System.out.println("jdbc:mysql://localhost:" +  assinged_port + "/" + database);
                con = DriverManager.getConnection("jdbc:mysql://localhost:" +  assinged_port + "/" + database, userName, password);
            } catch (ClassNotFoundException e) {
                try {
                    jumpSession.disconnect();
                } catch (Exception f)  {
                    f.printStackTrace();
                }
                jumpSession.disconnect();
                e.printStackTrace();
            } catch (SQLException e) {
                jumpSession.disconnect();
                e.printStackTrace();
            }
        }

        return con;
    }
}

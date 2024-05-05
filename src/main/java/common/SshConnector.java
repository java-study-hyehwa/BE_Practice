package common;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshConnector {
    private String host;
    private int port;
    private String user;
    private String publicKey;

    private Session session;


    public SshConnector() {

        //this.host = "ec2-3-38-127-6.ap-northeast-2.compute.amazonaws.com";
        this.host = "3.38.127.6";
        this.port = 22;
        this.user = "ec2-user";
        //this.publicKey = "C:\\Users\\ucheol\\.ssh\\BEpractice-bastion-key.pem";
        this.publicKey = "C:\\Users\\ucheol\\java\\PracticeBank\\src\\resources\\BEpractice-bastion-key.pem";

        try {
            JSch jsch = new JSch();
            jsch.setKnownHosts("C:\\Users\\ucheol\\.ssh\\known_hosts");
            jsch.addIdentity(publicKey);
            session = jsch.getSession(user, host, port);
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }

    }

    public Session getSession() {
        return session;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

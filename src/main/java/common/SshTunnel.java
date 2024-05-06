package common;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshTunnel {
    private String user;
    private String host;
    private int port;
    private String knownHosts;
    private String publicKey;

    private Session session;

    private SshTunnel (){}

    public static final SshTunnel construct() {
        return new SshTunnel();
    }

    public Session createTunnel(String user, String host, int port) {
        this.user = user;
        this.host = host;
        this.port = port;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

        } catch (JSchException e) {
            e.printStackTrace();
        }

        return session;
    }

    public Session createTunnel(String user, String host, int port, String knownHosts, String publicKey) {
        this.user = user;
        this.host = host;
        this.port = port;
        this.knownHosts = knownHosts;
        this.publicKey = publicKey;

        try {
            JSch jsch = new JSch();
            jsch.setKnownHosts(knownHosts);
            jsch.addIdentity(publicKey);
            session = jsch.getSession(user, host, port);
            session.connect();

        } catch (JSchException e) {
            e.printStackTrace();
        }

        return session;
    }

    public Session getSession() {
        return session;
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

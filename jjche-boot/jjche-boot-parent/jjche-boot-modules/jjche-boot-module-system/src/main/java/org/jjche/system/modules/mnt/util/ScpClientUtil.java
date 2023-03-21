package org.jjche.system.modules.mnt.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 远程执行linux命令
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @author: ZhangHouYing
 * @since: 2019-08-10 10:06
 */
public class ScpClientUtil {

    static private Map<String, ScpClientUtil> instance = Maps.newHashMap();
    private String ip;
    private int port;
    private String username;
    private String password;

    /**
     * <p>Constructor for ScpClientUtil.</p>
     *
     * @param ip       a {@link java.lang.String} object.
     * @param port     a int.
     * @param username a {@link java.lang.String} object.
     * @param password a {@link java.lang.String} object.
     */
    public ScpClientUtil(String ip, int port, String username, String password) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * <p>Getter for the field <code>instance</code>.</p>
     *
     * @param ip       a {@link java.lang.String} object.
     * @param port     a int.
     * @param username a {@link java.lang.String} object.
     * @param password a {@link java.lang.String} object.
     * @return a {@link ScpClientUtil} object.
     */
    static synchronized public ScpClientUtil getInstance(String ip, int port, String username, String password) {
        if (instance.get(ip) == null) {
            instance.put(ip, new ScpClientUtil(ip, port, username, password));
        }
        return instance.get(ip);
    }

    /**
     * <p>getFile.</p>
     *
     * @param remoteFile           a {@link java.lang.String} object.
     * @param localTargetDirectory a {@link java.lang.String} object.
     */
    public void getFile(String remoteFile, String localTargetDirectory) {
        Connection conn = new Connection(ip, port);
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(username, password);
            if (!isAuthenticated) {
                System.err.println("authentication failed");
            }
            SCPClient client = new SCPClient(conn);
            client.get(remoteFile, localTargetDirectory);
        } catch (IOException ex) {
            Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conn.close();
        }
    }

    /**
     * <p>putFile.</p>
     *
     * @param localFile             a {@link java.lang.String} object.
     * @param remoteTargetDirectory a {@link java.lang.String} object.
     */
    public void putFile(String localFile, String remoteTargetDirectory) {
        putFile(localFile, null, remoteTargetDirectory);
    }

    /**
     * <p>putFile.</p>
     *
     * @param localFile             a {@link java.lang.String} object.
     * @param remoteFileName        a {@link java.lang.String} object.
     * @param remoteTargetDirectory a {@link java.lang.String} object.
     */
    public void putFile(String localFile, String remoteFileName, String remoteTargetDirectory) {
        putFile(localFile, remoteFileName, remoteTargetDirectory, null);
    }

    /**
     * <p>putFile.</p>
     *
     * @param localFile             a {@link java.lang.String} object.
     * @param remoteFileName        a {@link java.lang.String} object.
     * @param remoteTargetDirectory a {@link java.lang.String} object.
     * @param mode                  a {@link java.lang.String} object.
     */
    public void putFile(String localFile, String remoteFileName, String remoteTargetDirectory, String mode) {
        Connection conn = new Connection(ip, port);
        try {
            conn.connect();
            boolean isAuthenticated = conn.authenticateWithPassword(username, password);
            if (!isAuthenticated) {
                System.err.println("authentication failed");
            }
            SCPClient client = new SCPClient(conn);
            if ((mode == null) || (mode.length() == 0)) {
                mode = "0600";
            }
            if (remoteFileName == null) {
                client.put(localFile, remoteTargetDirectory);
            } else {
                client.put(localFile, remoteFileName, remoteTargetDirectory, mode);
            }
        } catch (IOException ex) {
            Logger.getLogger(ScpClientUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            conn.close();
        }
    }


}

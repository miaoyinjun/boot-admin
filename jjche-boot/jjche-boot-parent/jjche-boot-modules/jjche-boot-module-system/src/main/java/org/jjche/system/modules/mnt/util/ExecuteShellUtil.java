package org.jjche.system.modules.mnt.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.log.StaticLog;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * 执行shell命令
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @author: ZhangHouYing
 * @since: 2019/8/10
 */
public class ExecuteShellUtil {

    Session session;
    private Vector<String> stdout;

    /**
     * <p>Constructor for ExecuteShellUtil.</p>
     *
     * @param ipAddress a {@link java.lang.String} object.
     * @param username  a {@link java.lang.String} object.
     * @param password  a {@link java.lang.String} object.
     * @param port      a int.
     */
    public ExecuteShellUtil(final String ipAddress, final String username, final String password, int port) {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, ipAddress, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(3000);
        } catch (Exception e) {
            StaticLog.error(e.getMessage(), e);
        }

    }

    /**
     * <p>execute.</p>
     *
     * @param command a {@link java.lang.String} object.
     * @return a int.
     */
    public int execute(final String command) {
        int returnCode = 0;
        ChannelShell channel = null;
        PrintWriter printWriter = null;
        BufferedReader input = null;
        stdout = new Vector<String>();
        try {
            channel = (ChannelShell) session.openChannel("shell");
            channel.connect();
            input = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            printWriter = new PrintWriter(channel.getOutputStream());
            printWriter.println(command);
            printWriter.println("exit");
            printWriter.flush();
            StaticLog.info("The remote command is: ");
            String line;
            while ((line = input.readLine()) != null) {
                stdout.add(line);
            }
        } catch (Exception e) {
            StaticLog.error(e.getMessage(), e);
            return -1;
        } finally {
            IoUtil.close(printWriter);
            IoUtil.close(input);
            if (channel != null) {
                channel.disconnect();
            }
        }
        return returnCode;
    }

    /**
     * <p>close.</p>
     */
    public void close() {
        if (session != null) {
            session.disconnect();
        }
    }

    /**
     * <p>executeForResult.</p>
     *
     * @param command a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String executeForResult(String command) {
        execute(command);
        StringBuilder sb = new StringBuilder();
        for (String str : stdout) {
            sb.append(str);
        }
        return sb.toString();
    }

}

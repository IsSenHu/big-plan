package com.gapache.ngrok.server.core;

import com.gapache.ngrok.server.task.CustomTask;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author HuSen
 * @since 2020/8/14 10:00 上午
 */
@Slf4j
public class SSHExec {

    private Session session;

    private Channel channel;

    private ConnBean conn;

    private static SSHExec ssh;

    private JSch jsch;

    private SSHExec(ConnBean conn) {
        try {
            this.conn = conn;
            jsch = new JSch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SSHExec getInstance(ConnBean conn) {
        if (ssh == null) {
            ssh = new SSHExec(conn);
        }
        return ssh;
    }

    public Boolean connect() {
        try {
            session = jsch.getSession(conn.getUser(), conn.getHost(), conn.getPort());
            UserInfo ui = new ConnCredential(conn.getPassword());
            log.info("Session initialized and associated with user credential {}", conn.getPassword());
            session.setUserInfo(ui);
            log.info("SSHExec initialized successfully");
            log.info("SSHExec trying to connect {}@{}", conn.getUser(), conn.getHost());
            session.connect(3600000);
            log.info("SSH connection established");
        } catch (Exception e) {
            log.error("Connect fails with the following exception: ", e);
            return false;
        }
        return true;
    }

    /**
     * Disconnect to remote machine and destroy session.
     *
     * @return if disconnect successfully, return true, else return false
     */
    public Boolean disconnect() {
        try {
            session.disconnect();
            session = null;
            log.info("SSH connection shutdown");
        } catch (Exception e) {
            log.error("Disconnect fails with the following exception: ", e);
            return false;
        }
        return true;
    }

    /**
     * Execute task on remote machine
     *
     * @param cmd - Task object that extends from CustomCode
     * @throws Exception
     */
    public synchronized Result exec(CustomTask task) throws TaskExecFailException {
        Result r = new Result();
        try {
            channel = session.openChannel("exec");
            String command = task.getCommand();
            ((ChannelExec) channel).setCommand(command);
            // X Forwarding
            // channel.setXForwarding(true);

            // channel.setInputStream(System.in);
            channel.setInputStream(null);

            channel.setOutputStream(System.out);

            FileOutputStream fos = new FileOutputStream(SysConfigOption.ERROR_MSG_BUFFER_TEMP_FILE_PATH);
            ((ChannelExec) channel).setErrStream(fos);
            // ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();

            channel.connect();
            StringBuilder sb = new StringBuilder();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    String str = new String(tmp, 0, i);
                    sb.append(str);
                    System.out.println(str);
                }
                if (channel.isClosed()) {
                    r.rc = channel.getExitStatus();
                    r.out = sb.toString();
                    if (task.isSuccess(sb.toString(), channel.getExitStatus())) {
                        r.errorMsg = "";
                        r.isSuccess = true;
                    } else {
                        r.errorMsg = SSHExecUtil.getErrorMsg(SysConfigOption.ERROR_MSG_BUFFER_TEMP_FILE_PATH);
                        r.isSuccess = false;
                        if (SysConfigOption.HALT_ON_FAILURE) {;
                            throw new TaskExecFailException(task.getInfo());
                        }
                    }
                    break;
                }

            }
            try {
                Thread.sleep(SysConfigOption.INTEVAL_TIME_BETWEEN_TASKS);
            } catch (Exception ignored) {
            }
            channel.disconnect();
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
        return r;
    }
}

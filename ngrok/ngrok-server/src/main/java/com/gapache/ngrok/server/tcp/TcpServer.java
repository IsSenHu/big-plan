package com.gapache.ngrok.server.tcp;

import com.gapache.ngrok.server.core.ConnBean;
import com.gapache.ngrok.server.core.Result;
import com.gapache.ngrok.server.core.SSHExec;
import com.gapache.ngrok.server.handler.TcpLogicHandler;
import com.gapache.ngrok.commons.TcpMessageDecoder;
import com.gapache.ngrok.server.http.HttpServer;
import com.gapache.ngrok.server.task.CustomTask;
import com.gapache.ngrok.server.task.impl.ExecCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @author HuSen
 * create on 2020/4/16 9:42 上午
 */
@Slf4j
@Setter
@Getter
public class TcpServer extends HttpServer {

    private volatile ChannelHandlerContext clientConnection;
    private volatile ChannelHandlerContext ngrokCliConnection;

    public TcpServer(int port, String name) {
        super(port, name);
    }

    @Override
    protected void doInitLogic(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new TcpMessageDecoder());
        pipeline.addLast(new TcpLogicHandler(this));
    }

    public static void main(String[] args) {
        SSHExec ssh = null;
        try {
            ConnBean cb = new ConnBean("118.24.38.46", "root", "521428Slyt", 22);
            ssh = SSHExec.getInstance(cb);
            ssh.connect();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String cmd = scanner.nextLine();
                if ("letMeGo".equals(cmd)) {
                    break;
                }
                CustomTask task = new ExecCommand(cmd);
                Result res = ssh.exec(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            assert ssh != null;
            ssh.disconnect();
        }
    }
}

package com.gapache.ngrok.cli.handler;

import com.gapache.commons.utils.IStringUtils;
import com.gapache.ngrok.cli.tcp.TcpClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuSen
 * create on 2020/4/16 10:32 上午
 */
@Slf4j
public class TcpServerMessageHandler extends SimpleChannelInboundHandler<byte[]> {

    private static final byte[] REGISTER_OK = IStringUtils.getBytes("ngrok-cli-register-ok");
    private static final int REGISTER_OK_LENGTH = REGISTER_OK.length;

    private final TcpClient tcpClient;

    public TcpServerMessageHandler(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) {
        if (msg.length >= REGISTER_OK_LENGTH) {
            for (int i = 0; i < REGISTER_OK_LENGTH; i++) {
                if (msg[i] != REGISTER_OK[i]) {
                    break;
                }
                if (i == REGISTER_OK_LENGTH - 1) {
                    log.info("注册成功");
                    return;
                }
            }
        }
        log.info("send msg:{}", IStringUtils.newString(msg));
        tcpClient.getRequestClient().send(msg);
    }
}

package com.gapache.ngrok.server.handler;

import com.gapache.commons.utils.IStringUtils;
import com.gapache.ngrok.server.tcp.TcpServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuSen
 * create on 2020/4/16 9:46 上午
 */
@Slf4j
public class TcpLogicHandler extends SimpleChannelInboundHandler<byte[]> {

    private static final byte[] REGISTER = IStringUtils.getBytes("ngrok-cli-register");
    private static final int REGISTER_LENGTH = REGISTER.length;

    private static final byte[] FROM_INNER = IStringUtils.getBytes("message-from-inner");
    private static final int FROM_INNER_LENGTH = FROM_INNER.length;

    private final TcpServer server;

    public TcpLogicHandler(TcpServer server) {
        this.server = server;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) {
        boolean isRegister = judge(msg, REGISTER_LENGTH, REGISTER);
        if (isRegister) {
            log.info("注册tcp ngrok客户端");
            server.setNgrokCliConnection(ctx);
            byte[] bytes = IStringUtils.getBytes("ngrok-cli-register-ok");
            server.getNgrokCliConnection().writeAndFlush(ctx.alloc().buffer(bytes.length).writeBytes(bytes));
            return;
        }

        boolean fromInner = judge(msg, FROM_INNER_LENGTH, FROM_INNER);
        if (fromInner) {
            log.info("来自ngrok-cli的消息返回给客户端");
            byte[] data = new byte[msg.length - FROM_INNER_LENGTH];
            System.arraycopy(msg, FROM_INNER_LENGTH, data, 0, data.length);
            if (server.getClientConnection() != null) {
                server.getClientConnection().writeAndFlush(ctx.alloc().buffer(data.length).writeBytes(data));
            }
            return;
        }

        log.info("来自客户端的消息:{}", IStringUtils.newString(msg));
        // 来自客户端的消息
        server.setClientConnection(ctx);
        // 消息转发给客户端
        server.getNgrokCliConnection().writeAndFlush(ctx.alloc().buffer(msg.length).writeBytes(msg));
    }

    private boolean judge(byte[] msg, int registerLength, byte[] register) {
        boolean isRegister = false;
        if (msg.length >= registerLength) {
            isRegister = true;
            for (int i = 0; i < registerLength; i++) {
                if (msg[i] != register[i]) {
                    isRegister = false;
                    break;
                }
            }
        }
        return isRegister;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught.", cause);
        super.exceptionCaught(ctx, cause);
    }
}

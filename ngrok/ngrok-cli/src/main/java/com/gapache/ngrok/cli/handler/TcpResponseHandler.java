package com.gapache.ngrok.cli.handler;

import com.gapache.commons.utils.IStringUtils;
import com.gapache.ngrok.cli.tcp.TcpRequestClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author HuSen
 * create on 2020/4/16 10:48 上午
 */
@Slf4j
public class TcpResponseHandler extends SimpleChannelInboundHandler<byte[]> {

    private final TcpRequestClient client;

    private static final byte[] FROM_INNER = IStringUtils.getBytes("message-from-inner");

    public TcpResponseHandler(TcpRequestClient client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) {
        log.info("TcpResponseHandler read:{}", IStringUtils.newString(msg));
        byte[] data = new byte[msg.length + FROM_INNER.length];
        System.arraycopy(FROM_INNER, 0, data, 0, FROM_INNER.length);
        System.arraycopy(msg, 0, data, FROM_INNER.length, msg.length);
        client.getTcpClient().getConnection().writeAndFlush(ctx.alloc().buffer(data.length).writeBytes(data));
    }
}

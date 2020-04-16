package com.gapache.ngrok.commons;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/16 9:49 上午
 */
@Slf4j
public class TcpMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        byte[] bytes = new byte[in.readableBytes()];
        if (bytes.length > 0) {
            in.getBytes(0, bytes);
            in.skipBytes(bytes.length);
            out.add(bytes);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("TcpMessageDecoder error.", cause);
    }
}

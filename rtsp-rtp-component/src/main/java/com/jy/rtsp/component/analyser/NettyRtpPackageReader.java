package com.jy.rtsp.component.analyser;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class NettyRtpPackageReader extends SimpleChannelInboundHandler<ByteBuf>{

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println(byteBuf.toString(Charset.defaultCharset()));
    }
    public NettyRtpPackageReader() {
        super(true);
    }
}

package com.jy.rtsp.component.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.FileOutputStream;

public class StreamMessageHandler extends SimpleChannelInboundHandler<byte[]> {

    private File file = new File("/home/amen/test/a.mov");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
        FileOutputStream fos = new FileOutputStream(file, true);
        fos.write(msg);
    }
}

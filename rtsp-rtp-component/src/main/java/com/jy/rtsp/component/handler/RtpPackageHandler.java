package com.jy.rtsp.component.handler;

import com.jy.rtsp.entity.RtpPackage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RtpPackageHandler extends SimpleChannelInboundHandler<RtpPackage> {

    private File file = new File("/home/amen/test/a.mov");
    private FileOutputStream fos;
    {

        try {
            if(!file.exists()) {
                if(!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            fos = new FileOutputStream(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RtpPackage msg) throws Exception {
        System.out.println("receive a RtpPackage: " + msg);
    }
}

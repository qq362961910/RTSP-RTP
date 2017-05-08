package com.jy.rtsp.component.handler;

import com.jy.rtsp.component.controller.VideoController;
import com.jy.rtsp.cpmmon.enums.VideoState;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.rtsp.RtspMethods;
import io.netty.handler.codec.rtsp.RtspVersions;

import java.nio.charset.Charset;
import java.util.Map;

public class RtspVideoHandler extends SimpleChannelInboundHandler<FullHttpMessage> implements VideoController {

    private static final String SESSION_KEY = "Session";
    private String rtspBaseUrl;
    private Channel channel;
    private int seqNo = 0;
    private String sessionId;
    private VideoState currentState = VideoState.OPTIONS;

    public RtspVideoHandler(String rtspBaseUrl) {
        this.rtspBaseUrl = rtspBaseUrl;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("channel has been establish");
        channel = ctx.channel();
//        final ByteBufAllocator allocator = ctx.alloc();
        new Thread(() -> {
//                String requestUrl = "OPTIONS rtsp://192.168.4.200:554/sample_h264_100kbit.mp4 RTSP/1.0\r\nCSeq: 1\r\n\r\n";
//                byte[] msgBytes = requestUrl.getBytes();
//                ByteBuf msg = allocator.buffer(msgBytes.length);
//                msg.writeBytes(msgBytes);
//                DefaultFullHttpRequest msg = new DefaultFullHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.OPTIONS, "rtsp://192.168.4.200:554/sample_h264_100kbit.mp4");
//                DefaultFullHttpRequest msg = new DefaultFullHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.DESCRIBE, "rtsp://192.168.4.200:554/sample_h264_100kbit.mp4");
//                HttpHeaders headers = msg.headers();
//                headers.add("CSeq", "10");
//                headers.add("Transport", "RTP/AVP/TCP;unicast;client_port=4588-4589");
//                channel.writeAndFlush(msg);
            //流程开始
            doOption();
        }).start();


    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpMessage message) throws Exception {
        DecoderResult decoderResult = message.decoderResult();
        if (decoderResult.isSuccess()) {

            switch (currentState) {

                case OPTIONS: {
                    System.out.println("---------------------------------------- OPTIONS -----------------------------------------");
                    readSuccessFullHttpMessage(message);
                    doDescribe();
                    currentState = VideoState.DESCRIBE;
                    break;
                }
                case DESCRIBE: {
                    System.out.println("---------------------------------------- DESCRIBE -----------------------------------------");
                    readSuccessFullHttpMessage(message);
                    doSetup();
                    currentState = VideoState.SETUP;
                    break;
                }
                case SETUP: {
                    System.out.println("---------------------------------------- SETUP -----------------------------------------");
                    for (Map.Entry<String, String> entry: message.headers().entries()) {
                        if (SESSION_KEY.equals(entry.getKey())) {
                            sessionId = entry.getValue();
                        }
                    }
                    readSuccessFullHttpMessage(message);
                    doPlay();
                    currentState = VideoState.PLAY;
                    break;
                }
                case PLAY: {
                    System.out.println("---------------------------------------- PLAY -----------------------------------------");
                    readSuccessFullHttpMessage(message);
                    doPause();
                    currentState = VideoState.PAUSE;
                    break;
                }
                case PAUSE: {
                    System.out.println("---------------------------------------- PAUSE -----------------------------------------");
                    readSuccessFullHttpMessage(message);
                    doTeardown();
                    currentState = VideoState.TEARDOWN;
                    break;
                }
                case TEARDOWN: {
                    System.out.println("---------------------------------------- TEARDOWN -----------------------------------------");
                    readSuccessFullHttpMessage(message);
                    break;
                }
                default: {
                    System.out.println("Unknown State: currentStata");
                }
            }
        }
        else {

        }
    }

    public void readSuccessFullHttpMessage(FullHttpMessage message) {
        HttpHeaders headers = message.headers();
        System.out.println("headers: ");
        for (Map.Entry<String, String> entry: headers) {
            System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
        }
        ByteBuf buf = message.content();
        if (buf.isReadable()) {
            System.out.println("body: ");
            System.out.println(buf.toString(Charset.defaultCharset()));
        }
    }

    @Override
    public void doOption() {
        DefaultFullHttpRequest msg = new DefaultFullHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.OPTIONS, rtspBaseUrl);
        HttpHeaders headers = msg.headers();
        headers.add("CSeq", seqNo++);
        channel.writeAndFlush(msg);
    }

    @Override
    public void doDescribe() {
        DefaultFullHttpRequest msg = new DefaultFullHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.DESCRIBE, rtspBaseUrl);
        HttpHeaders headers = msg.headers();
        headers.add("CSeq", seqNo++);
        channel.writeAndFlush(msg);
    }

    @Override
    public void doSetup() {
        DefaultFullHttpRequest msg = new DefaultFullHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.SETUP, rtspBaseUrl);
        HttpHeaders headers = msg.headers();
        headers.add("CSeq", seqNo++);
//        headers.add("Transport", "RTP/AVP;UNICAST;client_port=16264-16265;mode=play");
        //告诉服务端采用tcp发送协议
        headers.add("Transport", "RTP/AVP/TCP;interleaved=0-1");
        channel.writeAndFlush(msg);
    }

    @Override
    public void doPlay() {
        DefaultFullHttpRequest msg = new DefaultFullHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.PLAY, rtspBaseUrl);
        HttpHeaders headers = msg.headers();
        headers.add("CSeq", seqNo++);
        headers.add("Session", sessionId);
        headers.add("Transport", "RTP/AVP;UNICAST;client_port=16264-16265;mode=play");
        channel.writeAndFlush(msg);
    }

    @Override
    public void doPause() {
        DefaultFullHttpRequest msg = new DefaultFullHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.PAUSE, rtspBaseUrl);
        HttpHeaders headers = msg.headers();
        headers.add("CSeq", seqNo++);
        headers.add("Session", sessionId);
        channel.writeAndFlush(msg);
    }

    @Override
    public void doTeardown() {
        DefaultFullHttpRequest msg = new DefaultFullHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.TEARDOWN, rtspBaseUrl);
        HttpHeaders headers = msg.headers();
        headers.add("CSeq", seqNo++);
        headers.add("Session", sessionId);
        channel.writeAndFlush(msg);
    }
    
    
}

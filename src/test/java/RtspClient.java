import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.rtsp.RtspDecoder;
import io.netty.handler.codec.rtsp.RtspEncoder;
import io.netty.handler.codec.rtsp.RtspMethods;
import io.netty.handler.codec.rtsp.RtspVersions;

import java.nio.charset.Charset;
import java.util.Map;

public class RtspClient {

    private static final String host = "192.168.4.200";
    private static final int port = 554;
    private static String rtspBaseUrl = "rtsp://192.168.4.200:554/sample_h264_100kbit.mp4";

    public static void main(String[] args) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            ch.pipeline().addLast(new RtspDecoder());
                            ch.pipeline().addLast(new RtspEncoder());
                            ch.pipeline().addLast(new HttpObjectAggregator(4096));
                            pipeline.addLast(new RtspClientHandler(rtspBaseUrl));
                        }});
            ChannelFuture f = bootstrap.connect(host,port).sync();
            //wait until the connection is closed;
            f.channel().closeFuture().sync();
        } catch (Exception e){
            System.out.println(e);
        }finally {
            workerGroup.shutdownGracefully();
        }
    }
}

class RtspClientHandler extends ChannelInboundHandlerAdapter implements VideoController{

    private static final String SESSION_KEY = "Session";
    private String rtspBaseUrl;
    private Channel channel;
    private int seqNo = 0;
    private String sessionId;
    private State currentState = State.OPTIONS;

    public RtspClientHandler(String rtspBaseUrl) {
        this.rtspBaseUrl = rtspBaseUrl;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("channel has been establish");
       channel = ctx.channel();
//        final ByteBufAllocator allocator = ctx.alloc();
        new Thread(){
            @Override
            public void run() {

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
            }
        }.start();

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpMessage message  = (FullHttpMessage)msg;
        DecoderResult decoderResult = message.getDecoderResult();
//        if (decoderResult.isSuccess()) {
//            readSuccessFuulHttpMessage(message);
//        }
//        else {
//            System.out.println(decoderResult.cause());
//        }
        if (decoderResult.isSuccess()) {

            switch (currentState) {

                case OPTIONS: {
                    System.out.println("---------------------------------------- OPTIONS -----------------------------------------");
                    readSuccessFuulHttpMessage(message);
                    doDescribe();
                    currentState = State.DESCRIBE;
                    break;
                }
                case DESCRIBE: {
                    System.out.println("---------------------------------------- DESCRIBE -----------------------------------------");
                    readSuccessFuulHttpMessage(message);
                    doSetup();
                    currentState = State.SETUP;
                    break;
                }
                case SETUP: {
                    System.out.println("---------------------------------------- SETUP -----------------------------------------");
                    for (Map.Entry<String, String> entry: message.headers().entries()) {
                        if (SESSION_KEY.equals(entry.getKey())) {
                            sessionId = entry.getValue();
                        }
                    }
                    readSuccessFuulHttpMessage(message);
                    doPlay();
                    currentState = State.PLAY;
                    break;
                }
                case PLAY: {
                    System.out.println("---------------------------------------- PLAY -----------------------------------------");
                    readSuccessFuulHttpMessage(message);
                    doPause();
                    currentState = State.PAUSE;
                    break;
                }
                case PAUSE: {
                    System.out.println("---------------------------------------- PAUSE -----------------------------------------");
                    readSuccessFuulHttpMessage(message);
                    doTeardown();
                    currentState = State.TEARDOWN;
                    break;
                }
                case TEARDOWN: {
                    System.out.println("---------------------------------------- TEARDOWN -----------------------------------------");
                    readSuccessFuulHttpMessage(message);
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

    public void readSuccessFuulHttpMessage(FullHttpMessage message) {
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

/**
 * 视频控制器
 * */
interface VideoController{

    /**
     * OPTIONS
     * */
    void doOption();

    /**
     * DESCRIBE
     * */
    void doDescribe();

    /**
     * SETUP
     * */
    void doSetup();

    /**
     * PLAY
     * */
    void doPlay();

    /**
     * PAUSE
     * */
    void doPause();

    /**
     * TEARDOWN
     * */
    void doTeardown();

}

enum State {
    OPTIONS,
    DESCRIBE,
    SETUP,
    PLAY,
    PAUSE,
    TEARDOWN
}

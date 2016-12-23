import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ReferenceCountUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;

public class RtpTcpDataReceiver {

    private static final int port = 5566;

    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new TcpPersistDataHandler());
                        }});
            ChannelFuture f = bootstrap.bind(port).sync();
            //wait until the connection is closed;
            f.channel().closeFuture().sync();
        } catch (Exception e){
            System.out.println(e);
        }finally {
            workerGroup.shutdownGracefully();
        }
    }

}
class TcpPersistDataHandler extends ChannelInboundHandlerAdapter{

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//
//        File file = new File("D:/sample_h264_100kbit.mp4");
//        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
//        byte[] bs = new byte[bis.available()];
//        bis.read(bs);
//        ByteBuf buf = ctx.alloc().buffer(bs.length);
//        buf.writeBytes(bs);
//        ctx.writeAndFlush(buf);
//        System.out.println("connection established successfully, from: " + ctx.channel().remoteAddress());
//    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf)msg;
        System.out.println(buf.toString(Charset.defaultCharset()));
        ReferenceCountUtil.release(msg);

    }
}

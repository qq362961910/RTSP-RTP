import com.jy.rtsp.component.decoder.ByteToRtpPackageDecoder;
import com.jy.rtsp.component.handler.RtspVideoHandler;
import com.jy.rtsp.component.handler.RtpPackageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.rtsp.RtspDecoder;
import io.netty.handler.codec.rtsp.RtspEncoder;

public class RtspClient {

    private static final String host = "192.168.115.108";
    private static final int port = 554;
//    private static String rtspBaseUrl = "rtsp://192.168.4.209:554/sample_h264_100kbit.mp4";
    private static String rtspBaseUrl = "rtsp://"+ host +":554/sample_h264_1mbit.mp4";

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
                            ch.pipeline().addLast(new ByteToRtpPackageDecoder());
                            ch.pipeline().addLast(new HttpObjectAggregator(4096));
                            pipeline.addLast(new RtspVideoHandler(rtspBaseUrl));
                            pipeline.addLast(new RtpPackageHandler());
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


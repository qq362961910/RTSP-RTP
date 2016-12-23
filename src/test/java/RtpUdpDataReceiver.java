import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class RtpUdpDataReceiver {

    private static final int port = 5566;

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new UdpPersistDataHandler());
                        }
                    });
            bootstrap.bind(port).sync().channel().closeFuture().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}


class UdpPersistDataHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("connection established successfully, from: " + ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        DatagramPacket packet = (DatagramPacket)msg;
        ByteBuf buf = packet.content();

        byte[] content = new byte[buf.readableBytes()];
        buf.readBytes(content);

        RTPpacket rtp_packet = new RTPpacket(content, content.length);

        //print important header fields of the RTP packet received:
        System.out.println("Got RTP packet with SeqNum # " + rtp_packet.getsequencenumber() + " TimeStamp " + rtp_packet.gettimestamp() + " ms, of type " + rtp_packet.getpayloadtype());

        //print header bitstream:
        rtp_packet.printheader();

        //get the payload bitstream from the RTPpacket object
        int payload_length = rtp_packet.getpayload_length();
        byte[] payload = new byte[payload_length];
        rtp_packet.getpayload(payload);

//        System.out.println(buf.toString(Charset.defaultCharset()));

    }
}

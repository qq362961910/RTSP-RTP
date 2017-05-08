package com.jy.rtsp.component.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

public class NettyTcpRtspDecoder extends ByteToMessageDecoder{

    private char magic = '$';
    private byte odd = 1;
    private byte even = 0;
    private State state = State.$;
    private DataType dataType;
    private short length = -1;

    private int count = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.isReadable()) {
            switch (state) {
                // magic number, 纠正读取位置, 并且跳过'$'
                case $: {
                    rectifyBytebuf(in);
                    state = State.CHANNEL_NUMBER;
                    if (!in.isReadable()) {
                        break;
                    }
                }
                // channel number, 通信信道
                case CHANNEL_NUMBER: {
                    byte channelNumber = in.readByte();
                    // 奇数信道作为控制信道
                    if (channelNumber == odd) {
                        dataType = DataType.CONTROL;
                    }
                    // 偶数信道作为数据传输信道
                    else if (channelNumber == even) {
                        dataType = DataType.STREAM_DATA;
                    }
                    else {
                        throw new RuntimeException("now valid channel number: " + channelNumber);
                    }
                    state = State.DATA_LENGTH;
                    if (!in.isReadable()) {
                        break;
                    }
                }
                // embedded data length, 数据长度
                case DATA_LENGTH: {
                    length = in.readShort();
                    state = State.DATA;
                    if (!in.isReadable()) {
                        break;
                    }
                }
                case DATA: {
                    if (in.readableBytes() >= length) {
                        if (dataType == DataType.CONTROL) {
                            ByteBuf message = ctx.alloc().buffer(length);
                            in.readBytes(message, length);
                            out.add(message);
                            break;
                        }
                        else if (dataType == DataType.STREAM_DATA) {
                            byte[] message = new byte[length];
                            in.readBytes(message);
                            out.add(message);
                            break;
                        }
                        else {
                            throw new RuntimeException("unknown data type: " + dataType);
                        }
                    }
                    else {
                        return;
                    }
                }
                default: {
                    throw new RuntimeException("unknown read state");
                }
            }
        }
        reset();
    }

    private void rectifyBytebuf(ByteBuf in) {
        int readerIndex = in.readerIndex();
        try {
            in.getByte(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            while (in.readByte() != magic) {
                System.out.println("check byte not '$'");
            }
        } catch (Exception e) {
            System.out.println("count: " + count);
        }
        in.readerIndex(readerIndex + 1);
        count++;
    }

    private void reset() {
        state = State.$;
        dataType = null;
        length = -1;
    }

    enum DataType {
        CONTROL,
        STREAM_DATA
    }

    enum State {
        $,
        CHANNEL_NUMBER,
        DATA_LENGTH,
        DATA
    }
}

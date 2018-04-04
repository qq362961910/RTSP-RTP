package com.jy.rtsp.entity;

/**
 * RTP数据包
 * 注：基本的RTP说明并不定义任何头扩展本身，如果遇到X=1，需要特殊处理
 */
public class RtpHeader {

    /**
     *  V：RTP协议的版本号，占2位，当前协议版本号为2
     *  2 bit
     * */
    private byte version;

    /**
     * P：填充标志，占1位，如果P=1，则在该报文的尾部填充一个或多个额外的八位组，它们不是有效载荷的一部分。
     * 1 bit
     * */
    private byte padding;

    /**
     * X：扩展标志，占1位，如果X=1，则在RTP报头后跟有一个扩展报头,需要特殊处理
     * 1 bit
     * */
    private byte extension;

    /**
     * CC：CSRC计数器，占4位，指示CSRC 标识符的个数
     * 4 bit
     * */
    private byte csrcCount;

    /**
     * M: 标记，占1位，不同的有效载荷有不同的含义，对于视频，标记一帧的结束；对于音频，标记会话的开始。
     * 1 bit
     * */
    private byte mark;

    /**
     * PT: 有效荷载类型，占7位，用于说明RTP报文中有效载荷的类型，如GSM音频、JPEM图像等,在流媒体中大部分是用来区分音频流和视频流的，这样便于客户端进行解析。
     * 7 bit
     * */
    private byte payloadType;

    /**
     * 序列号：占16位，用于标识发送者所发送的RTP报文的序列号，每发送一个报文，序列号增1。
     * 这个字段当下层的承载协议用UDP的时候，网络状况不好的时候可以用来检查丢包。
     * 同时出现网络抖动的情况可以用来对数据进行重新排序，序列号的初始值是随机的，同时音频包和视频包的sequence是分别记数的。
     * 2 byte
     * */
    private short sequenceNo;

    /**
     * 时戳(Timestamp)：占32位，必须使用90 kHz 时钟频率。时戳反映了该RTP报文的第一个八位组的采样时刻。接收者使用时戳来计算延迟和延迟抖动，并进行同步控制。
     * 4 byte
     * */
    private int timestamp;

    /**
     * 同步信源(SSRC)标识符：占32位，用于标识同步信源。该标识符是随机选择的，参加同一视频会议的两个同步信源不能有相同的SSRC。
     * 4 byte
     * */
    private int synchronizedCsrc;

    /**
     * 特约信源(CSRC)标识符：每个CSRC标识符占32位，可以有0～15个。每个CSRC标识了包含在该RTP报文有效载荷中的所有特约信源。
     * 0～15个
     * */
    private int[] specialCsrcs;

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getPadding() {
        return padding;
    }

    public void setPadding(byte padding) {
        this.padding = padding;
    }

    public byte getExtension() {
        return extension;
    }

    public void setExtension(byte extension) {
        this.extension = extension;
    }

    public byte getCsrcCount() {
        return csrcCount;
    }

    public void setCsrcCount(byte csrcCount) {
        this.csrcCount = csrcCount;
    }

    public byte getMark() {
        return mark;
    }

    public void setMark(byte mark) {
        this.mark = mark;
    }

    public byte getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(byte payloadType) {
        this.payloadType = payloadType;
    }

    public short getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(short sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getSynchronizedCsrc() {
        return synchronizedCsrc;
    }

    public void setSynchronizedCsrc(int synchronizedCsrc) {
        this.synchronizedCsrc = synchronizedCsrc;
    }

    public int[] getSpecialCsrcs() {
        return specialCsrcs;
    }

    public void setSpecialCsrcs(int[] specialCsrcs) {
        this.specialCsrcs = specialCsrcs;
    }
}

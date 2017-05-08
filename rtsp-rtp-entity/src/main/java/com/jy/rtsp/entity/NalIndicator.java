package com.jy.rtsp.entity;

import com.jy.rtsp.cpmmon.enums.NalType;

/**
 * NALU 头由一个字节组成, 它的语法如下:
 * +---------------+
 * |0|1|2|3|4|5|6|7|
 * +-+-+-+-+-+-+-+-+
 * |F|NRI|  Type   |
 * +---------------+
 * F: 1 个比特.
 * forbidden_zero_bit. 在 H.264 规范中规定了这一位必须为 0.
 * NRI: 2 个比特.
 * nal_ref_idc. 取 00 ~ 11, 似乎指示这个 NALU 的重要性, 如 00 的 NALU 解码器可以丢弃它而不影响图像的回放. 不过一般情况下不太关心这个属性.
 * Type: 5 个比特.
 * nal_unit_type. 这个 NALU 单元的类型. 简述如下:
 * 0     没有定义
 * 1-23  NAL单元  单个 NAL 单元包.
 * 24    STAP-A   单一时间的组合包
 * 25    STAP-B   单一时间的组合包
 * 26    MTAP16   多个时间的组合包
 * 27    MTAP24   多个时间的组合包
 * 28    FU-A     分片的单元
 * 29    FU-B     分片的单元
 * 30-31 没有定义
 */
public class NalIndicator {

    /**
     * F:
     * forbidden_zero_bit. 在 H.264 规范中规定了这一位必须为 0.
     * */
    private byte f;

    /**
     * NRI: 2 个比特.
     * nal_ref_idc. 取 00 ~ 11, 似乎指示这个 NALU 的重要性, 如 00 的 NALU 解码器可以丢弃它而不影响图像的回放. 不过一般情况下不太关心这个属性.
     * */
    private byte nri;

    /**
     * Type: 5 个比特.
     * nal_unit_type. 这个 NALU 单元的类型. 简述如下:
     * 0     没有定义
     * 1-23  NAL单元  单个 NAL 单元包.
     * 24    STAP-A   单一时间的组合包
     * 25    STAP-B   单一时间的组合包
     * 26    MTAP16   多个时间的组合包
     * 27    MTAP24   多个时间的组合包
     * 28    FU-A     分片的单元
     * 29    FU-B     分片的单元
     * */
    private NalType type;

    public byte getF() {
        return f;
    }

    public void setF(byte f) {
        this.f = f;
    }

    public byte getNri() {
        return nri;
    }

    public void setNri(byte nri) {
        this.nri = nri;
    }

    public NalType getType() {
        return type;
    }

    public void setType(NalType type) {
        this.type = type;
    }
}

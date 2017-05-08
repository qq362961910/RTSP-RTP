package com.jy.rtsp.cpmmon.enums;

/**
 * 这个 RTP payload 中 NAL 单元的类型.
 * 这个字段和 H.264 中类型字段的区别是: 当 type 的值为 24 ~ 31 表示这是一个特别格式的 NAL 单元, 而 H.264 中, 只取 1~23 是有效的值.
 *
 * 1. 单一 NAL 单元模式
 * 即一个 RTP 包仅由一个完整的 NALU 组成. 这种情况下 RTP NAL 头类型字段和原始的 H.264的 NALU 头类型字段是一样的.
 * 2. 组合封包模式
 * 即可能是由多个 NAL 单元组成一个 RTP 包. 分别有4种组合方式: STAP-A, STAP-B, MTAP16, MTAP24. 那么这里的类型值分别是 24, 25, 26 以及 27.
 * 3. 分片封包模式
 * 用于把一个 NALU 单元封装成多个 RTP 包. 存在两种类型 FU-A 和 FU-B. 类型值分别是 28 和 29.
 *
 * 0：未规定
 * 1：非IDR图像中不采用数据划分的片段
 * 2：非IDR图像中A类数据划分片段
 * 3：非IDR图像中B类数据划分片段
 * 4：非IDR图像中C类数据划分片段
 * 5：IDR图像的片段
 * 6：补充增强信息（SEI）
 * 7：序列参数集（SPS）
 * 8：图像参数集（PPS）
 * 9：分割符
 * 10：序列结束符
 * 11：流结束符
 * 12：填充数据
 * 13：序列参数集扩展
 * 14：带前缀的NAL单元
 * 15：子序列参数集
 * 16 – 18：保留
 * 19：不采用数据划分的辅助编码图像片段
 * 20：编码片段扩展
 * 21 – 23：保留
 * 24 – 31：未规定
 * */
public enum NalType {

    /**
     * 0：未规定
     * */
    UNDEFINED_0((byte)0, "未指定"),
    /**
     * 1：非IDR图像中不采用数据划分的片段
     * */
    SLICE_LAYER_WITHOUT_PARTITIONING_IN_NO_IDR_RBSP((byte)1, "非IDR图像中不采用数据划分的片段"),
    /**
     * 2：非IDR图像中A类数据划分片段
     * */
    SLICE_DATA_PARTITION_A_LAYER_RBSP((byte)2, "非IDR图像中A类数据划分片段"),
    /**
     * 3：非IDR图像中B类数据划分片段
     * */
    SLICE_DATA_PARTITION_B_LAYER_RBSP((byte)3, "非IDR图像中B类数据划分片段"),
    /**
     * 4：非IDR图像中C类数据划分片段
     * */
    SLICE_DATA_PARTITION_C_LAYER_RBSP((byte)4, "非IDR图像中C类数据划分片段"),
    /**
     * 5：IDR图像的片段
     * */
    IDR_IMAGE_FRAME_RBSP((byte)5, "IDR图像的片段"),
    /**
     * 6：补充增强信息（SEI）
     * */
    SEI_RBSP((byte)6, "补充增强信息（SEI）"),
    /**
     * 7：序列参数集（SPS）
     * */
    SEQ_PARAMETER_SET_RBSP((byte)7, "序列参数集（SPS）"),
    /**
     * 8：图像参数集（PPS）
     * */
    PIC_PARAMETER_SET_RBSP((byte)8, "图像参数集（PPS）"),
    /**
     * 9：分割符
     * */
    ACCESS_UNIT_DELIMITER_RBSP((byte)9, "分割符"),
    /**
     * 10：序列结束符
     * */
    END_OF_SEQ_RBSP((byte)10, "序列结束符"),
    /**
     * 11：流结束符
     * */
    END_OF_STREAM_RBSP((byte)11, "流结束符"),
    /**
     * 12：填充数据
     * */
    FILLER_DATA_RBSP((byte)12, "填充数据"),
    /**
     * 13：序列参数集扩展
     * */
    SEQ_PARAMETER_SET_EXTENSION_RBSP((byte)13, "序列参数集扩展"),
    /**
     * 14：带前缀的NAL单元
     * */
    PREFIXED_NAL_RBSP((byte)14, "带前缀的NAL单元"),
    /**
     * 15：子序列参数集
     * */
    SUBSEQUENCE_PARAM_RBSP((byte)15, "子序列参数集"),
    /**
     * 16：保留
     * */
    RESERVED_16((byte)16, "保留"),
    /**
     * 17：保留
     * */
    RESERVED_17((byte)17, "保留"),
    /**
     * 18：保留
     * */
    RESERVED_18((byte)18, "保留"),
    /**
     * 19：不采用数据划分的辅助编码图像片段
     * */
    SLICE_LAYER_WITHOUT_PARTITIONING_ASSISTANT_ENCODED_IMAGE_RBSP((byte)19, "不采用数据划分的辅助编码图像片段"),
    /**
     * 20：编码片段扩展
     * */
    ENCODING_FRAME_EXTENSION((byte)20, "编码片段扩展"),
    /**
     * 21：保留
     * */
    RESERVED_21((byte)21, "保留"),
    /**
     * 22：保留
     * */
    RESERVED_22((byte)22, "保留"),
    /**
     * 23：保留
     */
    RESERVED_23((byte)23, "保留"),
    /**
     * 24    STAP-A   单一时间的组合包
     * */
    STAP_A((byte)24, "STAP-A"),
    /**
     * 25    STAP-B   单一时间的组合包
     * */
    STAP_B((byte)25, "STAP-B"),
    /**
     * 26    MTAP16   多个时间的组合包
     * */
    MTAP16((byte)26, "MTAP16"),
    /**
     * 27    MTAP24   多个时间的组合包
     * */
    MTAP24((byte)27, "MTAP24"),
    /**
     * 28    FU-A     分片的单元
     * */
    FU_A((byte)28, "FU-A"),
    /**
     * 29    FU-B     分片的单元
     * */
    FU_B((byte)29, "FU-B"),
    /**
     * 30 没有定义
     * */
    UNDEFINED_30((byte)30, "undefined"),
    /**
     * 31 没有定义
     * */
    UNDEFINED_31((byte)30, "undefined");

    private byte value;
    private String name;

    NalType(byte value, String name) {
        this.value = value;
        this.name = name;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public static NalType getNalType(byte value) {
        for (NalType type: values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}

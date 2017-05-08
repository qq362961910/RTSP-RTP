package com.jy.rtsp.entity;

import com.jy.rtsp.cpmmon.enums.NalType;

/**
 *
 * 单个NAL单元包:
 * NAL单元包必须只包含一个。这意味聚合包和分片单元不可以用在单个NAL 单元包中。并且RTP序号必须符合NAL单元的解码顺序。NAL单元的第一字节和RTP荷载头第一个字节重合.
 * 打包H264码流时，只需在帧前面加上12字节的RTP头即可。
 *
 * 分片单元（FU-A）:
 * 打包时，原始的NAL头的前三位为FU indicator的前三位，原始的NAL头的后五位为FU header的后五位。
 * 80 60 01 0f 00 0e 10 00 00 0000 00 7c 85 88 82 €`..........|???
 * 00 0a 7f ca 94 05 3b7f 3e 7f fe 14 2b 27 26 f8 ...??.;.>.?.+'&?
 * 89 88 dd 85 62 e1 6dfc 33 01 38 1a 10 35 f2 14 ????b?m?3.8..5?.
 * 84 6e 21 24 8f 72 62f0 51 7e 10 5f 0d 42 71 12 ?n!$?rb?Q~._.Bq.
 * 17 65 62 a1 f1 44 dc df 4b 4a 38 aa 96 b7 dd 24 .eb??D??KJ8????$
 * 前12字节是RTP Header
 * 7c是FU indicator
 * 85是FU Header
 * FU indicator（0x7C）和FU Header（0x85）换成二进制如下
 * 0111 1100 1000 0101
 * 按顺序解析如下：
 * 0                            是F
 * 11                          是NRI
 * 11100                    是FU Type，这里是28，即FU-A
 * 1                            是S，Start，说明是分片的第一包
 * 0                            是E，End，如果是分片的最后一包，设置为1，这里不是
 * 0                            是R，Remain，保留位，总是0
 * 00101                    是NAl Type，这里是5，说明是关键帧（不知道为什么是关键帧请自行谷歌）
 *
 * 打包时，FUindicator的F、NRI是NAL Header中的F、NRI，Type是28；FU Header的S、E、R分别按照分片起始位置设置，Type是NAL Header中的Type。
 * 解包时，取FU indicator的前三位和FU Header的后五位，即0110 0101（0x65）为NAL类型。
 * */
public class NalHeader {

    /**
     * S: 1 bit 当设置成1,开始位指示分片NAL单元的开始。当跟随的FU荷载不是分片NAL单元荷载的开始，开始位设为0。
     * */
    private boolean start;

    /**
     * E: 1 bit 当设置成1, 结束位指示分片NAL单元的结束，即, 荷载的最后字节也是分片NAL单元的最后一个字节。当跟随的 FU荷载不是分片NAL单元的最后分片,结束位设置为0。
     * */
    private boolean end;

    /**
     * R: 1 bit 保留位必须设置为0，接收者必须忽略该位
     * */
    private boolean r;

    /**
     * type
     * */
    private NalType type;

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isR() {
        return r;
    }

    public void setR(boolean r) {
        this.r = r;
    }

    public NalType getType() {
        return type;
    }

    public void setType(NalType type) {
        this.type = type;
    }
}

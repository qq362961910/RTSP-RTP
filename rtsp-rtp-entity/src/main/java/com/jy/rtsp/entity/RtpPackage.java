package com.jy.rtsp.entity;

/**
 * Rpt Package
 * package {
 *     rtp header;
 *     nal indicator;
 *     nal header;
 *     [nal1,
 *      nal2,
 *      ...
 *     ]
 * }
 */
public class RtpPackage {

    /**
     * rtp header
     * */
    private RtpHeader rtpHeader;

    /**
     * nal indicator
     * */
    private NalIndicator nalIndicator;

    /**
     * nal header
     * */
    private NalHeader nalHeader;

    public RtpHeader getRtpHeader() {
        return rtpHeader;
    }

    public void setRtpHeader(RtpHeader rtpHeader) {
        this.rtpHeader = rtpHeader;
    }

    public NalIndicator getNalIndicator() {
        return nalIndicator;
    }

    public void setNalIndicator(NalIndicator nalIndicator) {
        this.nalIndicator = nalIndicator;
    }

    public NalHeader getNalHeader() {
        return nalHeader;
    }

    public void setNalHeader(NalHeader nalHeader) {
        this.nalHeader = nalHeader;
    }
}

package com.jy.rtsp.cpmmon.enums;

public enum RtpVersion {

    /**
     * version: 2
     * */
    V_II((byte)2);

    public static RtpVersion getRtpVersion(byte value) {
        for (RtpVersion version: values()) {
            if (version.value == value) {
                return version;
            }
        }
        return null;
    }

    private byte value;

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    RtpVersion(byte value) {
        this.value = value;
    }
}

package com.jy.rtsp.cpmmon.enums;

/**
 * Created by amen on 5/8/17.
 */
public enum PayloadType {
    ;

    private byte value;

    private String name;

    PayloadType(byte value, String name) {
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
    public static PayloadType getPayloadType(byte value) {
        for (PayloadType type: values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}

package com.jy.rtsp.entity;

import java.util.Arrays;

public class IndexableBytes {

    private byte[] content;
    private int index;
    private int max;

    public byte[] readBytes(int length) {
        int startIndex = index;
        int endIndex = startIndex + length;
        if (endIndex > max) {
            throw new IndexOutOfBoundsException("target index: " + endIndex);
        }
        index = endIndex;
        return Arrays.copyOfRange(content, startIndex, endIndex);
    }

    public byte readByte() {
        int targetIndex = index++;
        if (targetIndex > max) {
            throw new IndexOutOfBoundsException("target index: " + targetIndex);
        }
        return content[targetIndex];
    }
    public short readShort() {
        int endIndex = index + 2;
        if (endIndex > max) {
            throw new IndexOutOfBoundsException("target index: " + endIndex);
        }
        short v = (short) ((content[index] & 0xff) << 8 | content[index + 1] & 0xff);
        index = endIndex;
        return v;
    }
    public int readInt() {
        int endIndex = index + 4;
        if (endIndex > max) {
            throw new IndexOutOfBoundsException("target index: " + endIndex);
        }
        int v = (content[index] & 0xff) << 24 | (content[index + 1] & 0xff) << 16 | (content[index + 2] & 0xff) << 8 | content[index + 3] & 0xff;
        index = endIndex;
        return v;
    }

    public IndexableBytes(byte[] content) {
        this.content = content;
        this.index = 0;
        this.max = content.length;
    }

    public IndexableBytes(byte[] content, int index) {
        this.content = content;
        this.index = index;
        this.max = content.length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}

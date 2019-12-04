package com.ws.netty.demo6;

/**
 * @author Jun
 * data  2019-09-05 22:04
 * 自定义内容
 */
public class MyProtocol {

    private int length;

    private byte[] content;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}

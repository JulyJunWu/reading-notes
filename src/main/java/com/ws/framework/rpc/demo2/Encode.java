package com.ws.framework.rpc.demo2;

/**
 * @Description:
 * @Date: 2019/8/10 0010 14:03
 */
public enum Encode {

    UTF_8((byte) 1, "UTF-8"),

    GBK((byte) 2, "GBK");

    private byte value;
    private String charStr;

    Encode(byte value, String charStr) {
        this.charStr = charStr;
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public String getCharStr() {
        return charStr;
    }

    public static Encode get(byte value) {

        for (Encode encode : values()) {
            if (encode.getValue() == value) {
                return encode;
            }
        }
        return null;
    }
}

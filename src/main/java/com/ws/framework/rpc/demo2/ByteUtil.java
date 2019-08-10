package com.ws.framework.rpc.demo2;

/**
 * @Description:
 * @Date: 2019/8/10 0010 15:06
 * 字节数组和int互转
 */
public class ByteUtil {

    public static int byteArray2Int(byte[] bytes) {

        int num = bytes[3] & 0xFF;
        num |= ((bytes[2] << 8) & 0xFF00);
        num |= ((bytes[1] << 16) & 0xFF0000);
        num |= ((bytes[0] << 24) & 0xFF000000);

        return num;
    }

    public static byte[] int2ByteArray(int num) {
        byte[] bytes = new byte[4];

        bytes[0] = (byte) ((num >> 24) & 0xFF);
        bytes[1] = (byte) ((num >> 16) & 0xFF);
        bytes[2] = (byte) ((num >> 8) & 0xFF);
        bytes[3] = (byte) ((num & 0xFF));

        return bytes;
    }
}

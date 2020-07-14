package com.ws.agent.agentmain;

/**
 * 工具类
 */
public class ByteUtils {
    /**
     * 字节数组转 int
     *
     * @param b
     * @param start 起始索引
     * @param len   结束索引
     * @return
     */
    public static int bytes2Int(byte[] b, int start, int len) {
        int sum = 0;
        int end = start + len;
        for (int i = start; i < end; i++) {
            int n = ((int) b[i]) & 0xff;
            n <<= (--len) * 8;
            sum += n;
        }
        return sum;
    }


    public static int bytes2Int(byte[] b) {
        return bytes2Int(b, 0, b.length);
    }
}

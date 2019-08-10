package com.ws.framework.rpc.demo2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description:
 * @Date: 2019/8/10 0010 14:12
 */
public class ProtocolUtil {

    /**
     * 请求发送
     */
    public static void writeRequest(OutputStream outputStream, Request request) throws IOException {
        outputStream.write(new byte[]{request.getEncode()});

        //编码
        String encode = Encode.UTF_8.getValue() == request.getEncode() ? Encode.UTF_8.getCharStr() : Encode.GBK.getCharStr();
        byte[] bytes = request.getCommand().getBytes(encode);

        outputStream.write(ByteUtil.int2ByteArray(bytes.length));
        outputStream.write(bytes);
        outputStream.flush();
    }

    /**
     * 响应接收
     */
    public static Response readResponse(InputStream inputStream) throws Exception {

        byte[] bytes = new byte[1];
        inputStream.read(bytes);
        byte aByte = bytes[0];
        Encode encode = Encode.get(aByte);

        bytes = new byte[4];
        inputStream.read(bytes);

        int commandLength = ByteUtil.byteArray2Int(bytes);
        bytes = new byte[commandLength];
        inputStream.read(bytes);

        String response = new String(bytes, encode.getCharStr());

        return new Response(encode.getValue(), response);
    }

    /**
     * 响应请求
     */
    public static void writeResponse(OutputStream outputStream, Response response) throws Exception {
        outputStream.write(new byte[]{response.getEncode()});
        //outputStream.write(ByteUtil.int2ByteArray(response.getResponseLength()));

        //编码
        String encode = Encode.UTF_8.getValue() == response.getEncode() ? Encode.UTF_8.getCharStr() : Encode.GBK.getCharStr();
        byte[] bytes = response.getResponse().getBytes(encode);
        outputStream.write(ByteUtil.int2ByteArray(bytes.length));
        outputStream.write(bytes);
        outputStream.flush();
    }


    /**
     * 读取请求数据
     */
    public static Request readRequest(InputStream inputStream) throws Exception {

        byte[] bytes = new byte[1];
        inputStream.read(bytes);

        Encode encode = Encode.get(bytes[0]);

        bytes = new byte[4];
        inputStream.read(bytes);

        int commandLength = ByteUtil.byteArray2Int(bytes);
        bytes = new byte[commandLength];
        inputStream.read(bytes);

        String command = new String(bytes, encode.getCharStr());

        return new Request(encode.getValue(), command);
    }

}

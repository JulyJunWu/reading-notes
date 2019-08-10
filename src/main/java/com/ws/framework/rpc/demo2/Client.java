package com.ws.framework.rpc.demo2;

import java.io.OutputStream;
import java.net.Socket;

/**
 * @Description:
 * @Date: 2019/8/10 0010 11:16
 */
public class Client {

    public static void main(String[] args) throws Exception {


        Socket socket = new Socket("127.0.0.1", 8888);

        OutputStream outputStream = socket.getOutputStream();
        //构建请求
        ProtocolUtil.writeRequest(outputStream, new Request(Encode.UTF_8.getValue(), "十年修得同船渡"));

        //接收响应结果
        Response response = ProtocolUtil.readResponse(socket.getInputStream());

        System.out.println(response.getResponse());

    }
}

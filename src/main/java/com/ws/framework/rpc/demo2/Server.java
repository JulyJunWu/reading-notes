package com.ws.framework.rpc.demo2;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Date: 2019/8/10 0010 11:23
 */
public class Server {

    public static final AtomicInteger NUM = new AtomicInteger(0);

    public static final String[] responseStr = {"what`s your problem?", "百年修得共枕眠", "修福报"};

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(8888);

        while (true) {

            Socket accept = serverSocket.accept();
            Request request = ProtocolUtil.readRequest(accept.getInputStream());

            Response response;
            if (request.getCommand().contains("hello")) {
                response = new Response(Encode.UTF_8.getValue(), "百年修得共枕眠");
            } else {
                int increment = NUM.getAndIncrement();
                response = new Response(Encode.UTF_8.getValue(), responseStr[increment % responseStr.length]);
            }
            ProtocolUtil.writeResponse(accept.getOutputStream(), response);
        }
    }


}

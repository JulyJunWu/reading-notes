package com.ws.framework.rpc.demo1;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @Description:
 * @Date: 2019/8/10 0010 11:16
 */
public class ClientConsumer {

    public static void main(String[] args) throws Exception {
        String interfaceName = ISayHelloService.class.getName();

        Method method = ISayHelloService.class.getMethod("sayHello", String.class);
        Object[] params = new Object[]{"Hello"};
        Socket socket = new Socket("127.0.0.1", 8888);

        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeUTF(interfaceName);
        objectOutputStream.writeUTF(method.getName());
        objectOutputStream.writeObject(method.getParameterTypes());
        objectOutputStream.writeObject(params);
        objectOutputStream.flush();

        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        String result = objectInputStream.readUTF();

        System.out.println(result);
    }
}

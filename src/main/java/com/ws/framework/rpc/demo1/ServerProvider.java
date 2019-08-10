package com.ws.framework.rpc.demo1;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Date: 2019/8/10 0010 11:23
 */
public class ServerProvider {

    public static Map<String, Class> map = new HashMap<>(2);

    static {
        map.put(ISayHelloService.class.getName(), SayHelloServiceImpl.class);
    }

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(8888);

        while (true) {
            Socket accept = serverSocket.accept();
            System.out.println("来客了");
            ObjectInputStream objectInputStream = new ObjectInputStream(accept.getInputStream());
            String interfaceName = objectInputStream.readUTF();
            String methodName = objectInputStream.readUTF();
            Class[] classType = (Class[]) objectInputStream.readObject();
            Object[] params = (Object[]) objectInputStream.readObject();

            Object instance = map.get(interfaceName).newInstance();
            Method method = instance.getClass().getMethod(methodName, classType);

            method.setAccessible(true);
            String invoke = (String) method.invoke(instance, params);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(accept.getOutputStream());
            objectOutputStream.writeUTF(invoke);
            objectOutputStream.flush();
            System.out.println("大爷常来玩!");

        }
    }

}

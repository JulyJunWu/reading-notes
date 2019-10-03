package com.ws.book.深入理解java虚拟机;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Jun
 * data  2019-10-03 17:55
 * 方法区溢出
 * 不断的创建类,促使方法区内存溢出 , 使用1.7
 */
public class OomInMethodArea implements ISayAble {

    public static void main(String[] args) {

        OomInMethodArea methodArea = new OomInMethodArea();

        MyProxy proxy = new MyProxy(methodArea);


        while (true) {
            ISayAble instance = (ISayAble) Proxy.newProxyInstance(OomInMethodArea.class.getClassLoader(), methodArea.getClass().getInterfaces(), proxy);
            instance.say();
        }
    }

    public void say() {
        System.out.println(666);
    }
}


interface ISayAble {
    void say();
}

class MyProxy implements InvocationHandler {

    private Object object;

    public MyProxy(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(8888);
        return method.invoke(object, args);
    }
}
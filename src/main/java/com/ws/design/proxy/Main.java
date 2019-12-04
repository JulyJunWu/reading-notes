package com.ws.design.proxy;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Jun
 * data  2019-09-22 19:05
 */
public class Main {

    @Test
    public void proxy() {

        RealSubject subject = new RealSubject();

        ProxySubject proxySubject = new ProxySubject(subject);

        System.out.println("我要租房");

        proxySubject.request();

        System.out.println("租房成功");
    }

    @Test
    public void proxy2() {
        RealSubject realSubject = new RealSubject();
        MyInvocationHandle invocationHandle = new MyInvocationHandle(realSubject);
        Subject proxyInstance = (Subject) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), realSubject.getClass().getInterfaces(), invocationHandle);
        proxyInstance.request();
    }
}

class MyInvocationHandle implements InvocationHandler{

    private Object target;

    public MyInvocationHandle(Object o){this.target = o;}

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("代理前");
        Object invoke = method.invoke(target, args);
        System.out.println("代理后");
        return invoke;
    }
}

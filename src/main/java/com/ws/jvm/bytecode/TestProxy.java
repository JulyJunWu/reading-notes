package com.ws.jvm.bytecode;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Jun
 * data  2019-08-17 21:40
 *
 * 动态代理生成的字节码只会重写接口的方法以及Object中的toString,hashCode,equals,其他的是因为都是final修饰(除了finalize和clone)
 *
 */
public class TestProxy {

    public static void main(String[] args) throws Exception {

        //设置生成动态代理的代理.class文的开关 sun.misc.ProxyGenerator.saveGeneratedFiles
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        RealObject realObject = new RealObject();
        ProxyObject proxyObject = new ProxyObject(realObject);

        SleepAble proxyInstance = (SleepAble) Proxy.newProxyInstance
                (realObject.getClass().getClassLoader(), realObject.getClass().getInterfaces(), proxyObject);

        proxyInstance.sleep();

        //验证一类是否是代理类
        boolean proxyClass = Proxy.isProxyClass(proxyInstance.getClass());
        System.out.println(proxyClass);

        //方式二
        Class<?> aClass = Proxy.getProxyClass(SleepAble.class.getClassLoader(), SleepAble.class);

        SleepAble instance = (SleepAble) aClass.getConstructor(InvocationHandler.class).newInstance(proxyObject);
        instance.sleep();

        System.out.println(proxyInstance.getClass() == instance.getClass());

    }
}


interface SleepAble {
    void sleep();
}

class RealObject implements SleepAble {

    @Override
    public void sleep() {
        System.out.println("睡觉,狗命要紧");
    }
}

class ProxyObject implements InvocationHandler {

    private Object target;

    public ProxyObject(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");

        method.invoke(target, args);

        System.out.println("after");
        return null;
    }
}

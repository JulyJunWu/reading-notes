package com.ws.mybatis.cglib;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author JunWu
 * 使用cglig实现代理
 */
@Slf4j
public class UserServiceCglib implements MethodInterceptor {

    private Object target;

    public Object getInstance(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        //  回调方法
        enhancer.setCallback(this);
        //  创建代理对象;
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        log.info("before invoke class -> {} , method -> {}", o.getClass(), method.getName());
        Object invoke = method.invoke(target, objects);
        log.info("after invoke class -> {} , method -> {}", o.getClass(), method.getName());

        return invoke;
    }
}

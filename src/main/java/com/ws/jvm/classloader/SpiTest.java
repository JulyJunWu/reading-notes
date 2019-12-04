package com.ws.jvm.classloader;

import org.junit.Test;

import java.sql.Driver;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author Jun
 * data  2019-08-13 19:56
 */
public class SpiTest {


    @Test
    public void serviceLoader() throws Exception {

        //由ServiceLoader类Bootstrap加载
        ServiceLoader<Driver> load = ServiceLoader.load(Driver.class);
        System.out.println("load classLoader -> " + load.getClass().getClassLoader());

        Iterator<Driver> iterator = load.iterator();
        while (iterator.hasNext()) {
            //系统类加载器加载 , 更具体一点是由线程上下文类加载器
            //可以自定义实现Driver接口,然后将实现的类全限定名卸载META-INF/services/java.sql.Driver中
            Driver next = iterator.next();
            System.out.println("name -> " + next.getClass().getName() + " classloader -> " + next.getClass().getClassLoader());
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(classLoader);

    }

}

package com.ws.lock;

import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jun
 * data  2019-08-16 23:39
 */
public class TestCAS {


    /**
     * 获取Unsafe对象
     */
    @Test
    public void getUnsafe() throws Exception {

        Field value = Unsafe.class.getDeclaredField("theUnsafe");
        value.setAccessible(true);
        Unsafe o1 = (Unsafe) value.get(null);
    }
    
    @Test
    public void atomicInteger(){

        AtomicInteger atomicInteger = new AtomicInteger();

        int andIncrement = atomicInteger.getAndIncrement();

        System.out.println(andIncrement);

    }


}

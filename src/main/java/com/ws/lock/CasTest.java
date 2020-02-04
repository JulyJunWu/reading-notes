package com.ws.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jun
 * data  2019-08-16 23:39
 */
@Slf4j
public class CasTest {

    private volatile int age ;

    private volatile String address;

    /**
     * 获取Unsafe对象
     *
     * 通过Unsafe对象获取字段在内存中的地址值
     */
    @Test
    public void getUnsafe() throws Exception {

        Field value = Unsafe.class.getDeclaredField("theUnsafe");
        value.setAccessible(true);
        Unsafe o1 = (Unsafe) value.get(null);

        CasTest cas = new CasTest();

        Field age = cas.getClass().getDeclaredField("age");
        Field address = cas.getClass().getDeclaredField("address");
        // 获取成员变量age所在内存中的地址值
        long ageOffset = o1.objectFieldOffset(age);
        long addressOffset = o1.objectFieldOffset(address);
        log.info("age内存地址[{}] | address内存地址[{}]",ageOffset,addressOffset);
    }
    
    @Test
    public void atomicInteger(){

        AtomicInteger atomicInteger = new AtomicInteger();

        int andIncrement = atomicInteger.getAndIncrement();

        System.out.println(andIncrement);

    }


}

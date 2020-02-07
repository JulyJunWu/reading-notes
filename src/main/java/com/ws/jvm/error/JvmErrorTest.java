package com.ws.jvm.error;

import com.ws.jvm.reference.ReferenceTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import sun.misc.Cleaner;
import sun.misc.VM;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JunWu
 * 常见JVM报错信息
 * <p>
 * Throwable:
 * Error
 * Exception
 * check Exception  :  Sql/IO
 * unchecked Exception
 */
@Slf4j
public class JvmErrorTest {

    /**
     * 堆内存溢出
     * JVM参数 :
     * -Xms10m -Xmx10m -XX:+PrintGCDetails
     * 错误信息: java.lang.OutOfMemoryError: Java heap space
     */
    @Test
    public void heapOom() {
        byte[] bytes = new byte[1024 * 1024 * 100];
    }

    public static void test() {
        test();
    }

    /**
     * 栈溢出
     * JVM:       -Xms10m -Xmx10m -XX:+PrintGCDetails -Xss128k
     * 报错信息 : java.lang.StackOverflowError
     */
    @Test
    public void stackOverflow() {
        // 递归调用
        test();
    }

    /**
     * GC回收不足2% 报错
     * JVM参数 :  -Xms5m -Xmx5m -XX:+PrintGCDetails
     * 报错信息:  java.lang.OutOfMemoryError: GC overhead limit exceeded
     */
    @Test
    public void collectLittle() {
        List<Object> list = new ArrayList<>();
        int index = 0;
        while (true) {
            list.add(String.valueOf(index++));
        }
    }

    /**
     * 直接缓冲区溢出,主要是用于NIO,直接内存不受堆内存影响,受本地内存限制
     * JVM参数   : -XX:MaxDirectMemorySize=10m
     * 报错信息  : java.lang.OutOfMemoryError: Direct buffer memory
     */
    @Test
    public void directBufferOom() throws Exception {
        // 如果没有设置该值,估计(可能与操作系统有关)默认是系统内存的1/4;
        long memory = VM.maxDirectMemory();
        log.info("-XX:MaxDirectMemorySize = {}m", memory / 1024 / 1024);
        // 分配直接内存
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024 * 5);
        //手动回收直接内存(一般不用)
        Field cleanerField = buffer.getClass().getDeclaredField("cleaner");
        cleanerField.setAccessible(true);
        Cleaner cleaner = (Cleaner) cleanerField.get(buffer);
        cleaner.clean();
    }

    /**
     * 元空间溢出
     * JVM参数  : -XX:MaxMetaspaceSize
     * 报错信息 : Exception in thread "main" java.lang.OutOfMemoryError: Metaspace
     */
    @Test
    public void metaspaceOom() {

        /** 方式一 : 利用字符串常量池撑爆
         *   String str = "4545";
         *    while (true) {
         *        str += str + str.intern() + str;
         *    }
         */
        // 方式二: 使用字节码技术(cglib,基于类)生成 类信息撑爆
        while (true) {
            try{
                Enhancer enhancer = new Enhancer();
                enhancer.setSuperclass(ReferenceTest.Ws.class);
                enhancer.setUseCache(false);
                enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) ->  method.invoke(o, objects));
                enhancer.create();
            }catch (Throwable e){
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}

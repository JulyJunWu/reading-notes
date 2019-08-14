package com.ws.framework.heap;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Date: 2019/8/14 0014 8:59
 * 逃逸分析
 * <p>
 * JVM参数
 * 最小堆内存  最大堆内存  年轻代大小  线程大小
 * -Xms2048m -Xmx2048m -Xmn1024m -Xss1024k
 */
public class HeapDemo {

    public static final int LENGTH = 10000000;

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < LENGTH; i++) {
            doEscapeAnalysis();
        }

        System.gc();

        TimeUnit.SECONDS.sleep(10000);

    }

    /**
     * 开启逃逸分析 -XX:+DoEscapeAnalysis
     * 关闭逃逸分析 -XX:-DoEscapeAnalysis
     * <p>
     * jmap -histo pid  查看实例个数
     * jmap -histo:live pid
     * <p>
     * jmap -heap pid   查看堆内存的详情
     */
    public static void doEscapeAnalysis() {
        HeapDemo heapDemo = new HeapDemo();
    }

}

class HeapBean {

}

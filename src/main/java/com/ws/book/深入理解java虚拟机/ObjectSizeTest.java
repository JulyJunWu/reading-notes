package com.ws.book.深入理解java虚拟机;

import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;

/**
 * @Description:
 * @Author: JulyJunWu
 * @Date: 2020/7/22 23:40
 */
public class ObjectSizeTest {

    private String name;
    private int age;
    private long id;

    @Test
    public void printSize(){
        // 预计为
        /**
         *  markword + classpointer + 实例数据 + 对齐
         *
         *  4 + 4
         */
        ObjectSizeTest sizeTest = new ObjectSizeTest();
        String printable = ClassLayout.parseInstance(sizeTest).toPrintable();
        System.out.println(printable);
        System.out.println("--------------------------");
        ObjectSizeTest [] Test = new ObjectSizeTest[5];
        String printable2 = ClassLayout.parseInstance(Test).toPrintable();
        System.out.println(printable2);


    }

}

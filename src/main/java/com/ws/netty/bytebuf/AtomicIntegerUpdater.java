package com.ws.netty.bytebuf;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author Jun
 * data  2019-09-03 21:56
 * 引用计数
 */
public class AtomicIntegerUpdater {


    public static void main(String[] args) throws Exception {

        Person person = new Person();
        //AtomicIntegerFieldUpdater  AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");
        // 参数1 : 指定的泛型的class对象 , 参数二 : 需要更新的字段名称;
        //使用前提要求 : 1.被更新的字段修饰符不能是 private
        //              2.被更新的字段必须是int类型
        //                 3.被更新的字段必须是volatile修饰
        AtomicIntegerFieldUpdater<Person> integerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(integerFieldUpdater.getAndIncrement(person));
            }).start();
        }
    }
}

class Person {
    volatile int age = 1;
}

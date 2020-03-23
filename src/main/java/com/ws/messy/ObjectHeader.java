package com.ws.messy;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author JunWu
 * @since 2020/3/18 22:49
 *
 * 对象在内存的数据/布局
 */
public class ObjectHeader {

    private int age = 18;
    private String str = "Hello";
    private ObjectHeader o;

    public ObjectHeader(ObjectHeader o) {
        this.o = o;
    }

    public ObjectHeader() {
    }

    /**
     * java object layout
     *
     * @param args
     */
    public static void main(String[] args) {

        // 对象
        ObjectHeader header = new ObjectHeader(new ObjectHeader());
        ClassLayout classLayout = ClassLayout.parseInstance(header);
        //打印对象在内存的布局
        System.out.println(classLayout.toPrintable());

        System.out.println("-------------------------------------------------");
        //数组
        ObjectHeader[] headers = new ObjectHeader[5];
        ClassLayout layout = ClassLayout.parseInstance(headers);
        System.out.println(layout.toPrintable());

        System.out.println(0b00000001_00000000_000000000);

        synchronized (header){
            // 注意查看锁的标志位
            System.out.println(classLayout.toPrintable());
        }


    }

}

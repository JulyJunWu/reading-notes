package com.ws.framework;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.ws.framework.model.Person;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.stream.IntStream;

/**
 * @Description:
 * @Date: 2019/8/10 0010 9:59
 * 序列化
 */
public class JavaSerialize {

    /**
     * 原生序列化
     *
     * @throws Exception
     */
    @Test
    public static void serialize() throws Exception {
        Person testSerialize = new Person("ZWS", 18);
        //序列化
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
        objectOutputStream.writeObject(testSerialize);

        //反序列化
        byte[] bytes = arrayOutputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Person object = (Person) objectInputStream.readObject();

        //System.out.println(object);
    }

    /**
     * 使用Hessian序列化
     */
    @Test
    public static void hessianSerialize() throws Exception {

        Person person = new Person("ZWS", 18);

        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(arrayOutputStream);

        hessian2Output.writeObject(person);
        //对比ObjectOutputStream 要多个flush操作
        //hessian2Output.flushBuffer();
        hessian2Output.flush();

        byte[] bytes = arrayOutputStream.toByteArray();
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
        Hessian2Input hessian2Input = new Hessian2Input(arrayInputStream);

        Person result = (Person) hessian2Input.readObject();

        //System.out.println(result);
    }

    /**
     * 性能测试
     * 循环 100000/1000000/5000000/10000000次测试 性能提升稳定在1倍左右
     */
    @Test
    public void list() {

        long startTime = System.currentTimeMillis();

        IntStream.range(0, 1000000).forEach(i -> {
            try {
                serialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println("耗时 -> " + (System.currentTimeMillis() - startTime));

        startTime = System.currentTimeMillis();

        IntStream.range(0, 1000000).forEach(i -> {
            try {
                hessianSerialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println("耗时 -> " + (System.currentTimeMillis() - startTime));
    }

}



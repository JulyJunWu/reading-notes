package com.ws.framework;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.ws.framework.model.Person;
import io.netty.channel.nio.NioEventLoopGroup;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.regex.Pattern;
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


    @Test
    public void test2()throws Exception{


        System.out.println( 1 << 2);


    }

}



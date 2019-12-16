package com.ws.netty.serializable;

import com.ws.mybatis.model.SexEnum;
import com.ws.mybatis.model.User;
import com.ws.mybatis.util.HessianUtils;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.stream.IntStream;

/**
 * @author JunWu
 * 比较序列化
 */
public class CompareSeriaizable {

    public static void main(String[] args)throws Exception {
        int count = 5000000;
        User user = new User();
        user.setId("6688");
        user.setName("ws");
        user.setSex(SexEnum.FEMALE);
        user.setAge(18);

        long timeMillis = System.currentTimeMillis();
        IntStream.range(0,count).forEach(p->{
            try{
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream outputStream = new ObjectOutputStream(arrayOutputStream);
                outputStream.writeObject(user);
                outputStream.flush();
                outputStream.close();
                byte[] bytes = arrayOutputStream.toByteArray();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        long timeMillis1 = System.currentTimeMillis();
        System.out.println(timeMillis1 - timeMillis);

        timeMillis = System.currentTimeMillis();
        IntStream.range(0,count).forEach(p->{
            byte[] serialize = HessianUtils.serialize(user);
        });
        timeMillis1 = System.currentTimeMillis();
        System.out.println(timeMillis1 - timeMillis);
    }
}

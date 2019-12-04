package com.ws.netty.protobuf;

/**
 * @author Jun
 * data  2019-08-19 21:49
 */
public class TestProtoBuf {


    public static void main(String[] args) throws Exception {


        DataInfo.Student student = DataInfo.Student.newBuilder().setAge(20).setName("湖州市").build();
        byte[] bytes = student.toByteArray();

        DataInfo.Student parseFrom = DataInfo.Student.parseFrom(bytes);

        System.out.println(parseFrom.getAge());
        System.out.println(parseFrom.getName());
    }
}

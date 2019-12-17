package com.ws.book.netty权威指南.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author JunWu
 */
@org.msgpack.annotation.Message
@Data
public class Message implements Serializable {
    private String name;
    private String sex;
    private int age;
    private String address;
}

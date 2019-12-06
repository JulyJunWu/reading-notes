package com.ws.mybatis.model;

import lombok.Data;

import java.util.List;

/**
 * 实体bean - User
 *
 * @author JunWu
 */
@Data
public class User {

    private String id;

    private String name;

    private int age;

    private SexEnum sex;

    private List<Shop> shopList;

    private Shop shop;

}

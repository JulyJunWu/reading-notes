package com.ws.mybatis.service;

import com.ws.mybatis.model.SexEnum;
import com.ws.mybatis.model.User;

/**
 * @author JunWu
 * 接口服务
 */
public class UserService {

    public User getUser() {
        User user = new User();
        user.setAge(18);
        user.setSex(SexEnum.FEMALE);
        user.setName("ws");
        user.setId("12345");
        return user;
    }

}

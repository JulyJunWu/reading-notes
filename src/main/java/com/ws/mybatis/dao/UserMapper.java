package com.ws.mybatis.dao;

import com.ws.mybatis.model.User;

import java.util.List;

/**
 * @author JunWu
 * User Mapper
 */
public interface UserMapper {

    List<User> selectAll(User user);

    User selectById(String id);

    int insert(User user);

    int deleteUser(String id);

    int updateUser(User user);

}

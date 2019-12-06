package com.ws.mybatis.dao;

import com.ws.mybatis.model.User;

import java.util.List;
import java.util.Map;

/**
 * @author JunWu
 * User Mapper
 */
public interface UserMapper {

    List<User> selectAll(User user);

    User selectById(String id);

    Map selectMapById(String id);

    int insert(User user);

    int deleteUser(String id);

    int updateUser(User user);

}

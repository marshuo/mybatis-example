package com.marshuo.mapper;

import com.marshuo.entity.User;

import java.util.List;

/**
 * @author mars
 * @date 2022/09/20
 */
public interface UserMapper {
    User getUserById(int id);
    List<User> getUserList();
    int addUser(User user);
    int editUser(User user);
    int deleteUserById(int id);
    int addUserList(List<User> userList);
    int addUser2(User user);
    List<User> getUserListByName(String name);
}

package com.marshuo.mapper;

import com.marshuo.entity.User;

/**
 * @author mars
 * @date 2022/09/25
 */
public interface UserMapper {
    User getUserById(int id);
    int updateUserById(User user);
}

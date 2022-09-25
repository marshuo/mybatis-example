package com.marshuo.mapper;

import com.marshuo.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author mars
 * @date 2022/09/20
 */
public interface UserMapper {
    User getUserById(int id);
    
    // 分页查询用户列表
    List<User> getUserListLimit(Map<String, Integer> map);
}

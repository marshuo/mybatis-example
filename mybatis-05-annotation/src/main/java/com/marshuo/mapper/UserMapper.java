package com.marshuo.mapper;

import com.marshuo.entity.User;
import org.apache.ibatis.annotations.*;

/**
 * @author mars
 * @date 2022/09/20
 */
public interface UserMapper {
    @Select("select * from user where id= #{id}")
    User getUserById(int id);
    
    @Insert("insert into user(id, name, password) values (#{id}, #{username}, #{password})")
    int addUser(User user);
    
    @Update("update user set name = #{username}, password = #{password} where id = #{id}")
    int updateUser(User user);
    
    @Delete("delete from user where id = #{id}")
    int deleteUserById(@Param("id") int id);
}

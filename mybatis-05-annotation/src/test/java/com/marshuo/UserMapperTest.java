package com.marshuo;

import com.marshuo.entity.User;
import com.marshuo.mapper.UserMapper;
import com.marshuo.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @author mars
 * @date 2022/09/20
 */
public class UserMapperTest {
    
    @Test
    public void testGetUserById() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            // 方法1：推荐
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.getUserById(1);
            System.out.println("user = " + user);

        }
    }
    
    @Test
    public void testAddUser() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            
            mapper.addUser(new User(3, "老王", "abc"));
        }
    }
    
    @Test
    public void testUpdateUser() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.updateUser(new User(3, "老王头", "123"));
        }
    }
    
    @Test
    public void testDeleteUser() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.deleteUserById(10);
        }
    }
}

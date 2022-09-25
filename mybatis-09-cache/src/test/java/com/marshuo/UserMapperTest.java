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
    
    // 两次select相同的id，后一次去缓存中拿数据
    @Test
    public void testGetUserById() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.getUserById(1);
            System.out.println("user = " + user);
    
            User user1 = mapper.getUserById(1);
            System.out.println("user1 = " + user1);
    
            System.out.println("(user == user1) = " + (user == user1));
        }
    }
    

    // 两次查询不同的用户，查看是否有缓存
    @Test
    public void testGetUserById2() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.getUserById(1);
            System.out.println("user = " + user);
            
            User user1 = mapper.getUserById(2);
            System.out.println("user1 = " + user1);
            
            System.out.println("(user == user1) = " + (user == user1));
        }
    }
    
    
    // 第二次查询之前，去更新另一个用户的信息
    @Test
    public void testGetUserById3() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.getUserById(1);
            System.out.println("user = " + user);
    
            mapper.updateUserById(new User(9, "李大海", "aacc"));
            
            User user1 = mapper.getUserById(1);
            System.out.println("user1 = " + user1);
        
            System.out.println("(user == user1) = " + (user == user1));
        }
    }

    // 手动清理缓存
    @Test
    public void testGetUserById4() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.getUserById(1);
            System.out.println("user = " + user);
        
            // 清理缓存
            sqlSession.clearCache();
        
            User user1 = mapper.getUserById(1);
            System.out.println("user1 = " + user1);
        
            System.out.println("(user == user1) = " + (user == user1));
        }
    }
    
    
    @Test
    public void testUpdateUser() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.updateUserById(new User(9, "李大海", "aabb"));
        }
    }
    
}

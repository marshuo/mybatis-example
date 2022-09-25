package com.marshuo;

import com.marshuo.entity.User;
import com.marshuo.mapper.UserMapper;
import com.marshuo.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mars
 * @date 2022/09/20
 */
public class UserMapperTest {
    
    @Test
    public void testGetUserById() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.getUserById(1);
            System.out.println("user = " + user);
        }
    }
    
    @Test
    public void testSelectUserList() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = mapper.getUserList();
            for (User user : userList) {
                System.out.println("user = " + user);
            }
        }
    }
    
    @Test
    public void testAddUser() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = new User(5, "赵六", "123");
            mapper.addUser(user);
            // 提交事务，增删改都要提交事务，否则不生效
            sqlSession.commit();
        }
    }
    
    @Test
    public void testEditUser() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = new User(1, "张三斤", "abc");
            mapper.editUser(user);
            sqlSession.commit();
        }
    }
    
    @Test
    public void testDeleteUser() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            int id = 3;
            mapper.deleteUserById(id);
            sqlSession.commit();
        }
    }
    
    @Test
    public void testAddUserList() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            
            List<User> users = new ArrayList<>();
            users.add(new User(7, "钱七", "123"));
            users.add(new User(8, "孙八", "456"));
            users.add(new User(9, "李九", "abc"));
            
            int ret = mapper.addUserList(users);
            System.out.println("ret = " + ret);
            sqlSession.commit();
    
        }
    }
    
    // 测试自动创建主键
    @Test
    public void testAddUser2() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    
            User user = new User(1, "测试用户", "aaabbb");
            mapper.addUser2(user);
            System.out.println("user = " + user);
    
            sqlSession.commit();
        }
    }
    
    // 模糊查询
    @Test
    public void testGetUserListByName() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = mapper.getUserListByName("张");
    
            for (User user : userList) {
                System.out.println("user = " + user);
            }
        }
    }
}

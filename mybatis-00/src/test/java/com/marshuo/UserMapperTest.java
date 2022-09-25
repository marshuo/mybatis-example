package com.marshuo;

import com.marshuo.entity.User;
import com.marshuo.mapper.UserMapper;
import com.marshuo.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @author mars
 * @date 2022/09/22
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
}

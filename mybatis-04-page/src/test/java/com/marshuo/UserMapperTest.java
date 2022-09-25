package com.marshuo;

import com.marshuo.entity.User;
import com.marshuo.mapper.UserMapper;
import com.marshuo.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mars
 * @date 2022/09/20
 */
public class UserMapperTest {
    private static final Logger logger = LogManager.getLogger(UserMapperTest.class);
    
    @Test
    public void testGetUserById() {
        
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            // 方法1：推荐
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.getUserById(1);
            System.out.println("user = " + user);

        }
    }
    
    // 测试日志
    @Test
    public void testLog() {
        // Logger logger = LogManager.getRootLogger();
        logger.trace("trace log");
        logger.debug("debug log");
        logger.info("info log");
        logger.warn("warn log");
        logger.error("error log");
        logger.fatal("fatal log");
    }

    // 分页测试
    @Test
    public void testSelectUserListLimit() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            Map<String, Integer> map = new HashMap<>();
            map.put("startIndex", 4);
            map.put("pageSize", 3);
            List<User> users = mapper.getUserListLimit(map);
            for (User user : users) {
                logger.info(user);
            }
        }
    
    }
}

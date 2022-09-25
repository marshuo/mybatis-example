package com.marshuo;

import com.marshuo.entity.User;
import com.marshuo.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

/**
 * @author mars
 * @date 2022/09/20
 */
public class UserMapperTest {
    private static final Logger logger = LogManager.getLogger();
    
    @Test
    public void testGetUserById() {
        // Logger logger = LogManager.getRootLogger();
        
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            // 方法1：推荐
            // UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            // User user = mapper.getUserById(1);
            // System.out.println("user = " + user);

            // 方法2：不推荐
            User user = sqlSession.selectOne("com.marshuo.mapper.UserMapper.getUserById", 1);
            System.out.println("user = " + user);
        }
    }
    
    @Test
    public void testLog() {
    
        logger.trace("trace level");
        logger.debug("debug level");
        logger.info("info level");
        logger.warn("warn level");
        logger.error("error level");
        logger.fatal("fatal level");
        
    }
    
}

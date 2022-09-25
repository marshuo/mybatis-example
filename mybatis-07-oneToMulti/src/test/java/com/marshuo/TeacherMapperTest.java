package com.marshuo;

import com.marshuo.entity.Teacher;
import com.marshuo.mapper.TeacherMapper;
import com.marshuo.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @author mars
 * @date 2022/09/20
 */
public class TeacherMapperTest {
    @Test
    public void testGetStudentById() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
            Teacher teacher = mapper.getTeacherById(1);
            System.out.println("teacher = " + teacher);
        }
    }
}

package com.marshuo;

import com.marshuo.entity.Student;
import com.marshuo.mapper.StudentMapper;
import com.marshuo.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @author mars
 * @date 2022/09/20
 */
public class StudentMapperTest {
    @Test
    public void testGetStudentById() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            Student student = mapper.getStudentById(1);
            System.out.println("student = " + student);
        }
    }
}

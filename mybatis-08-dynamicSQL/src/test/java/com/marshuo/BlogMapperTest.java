package com.marshuo;

import com.marshuo.entity.Blog;
import com.marshuo.mapper.BlogMapper;
import com.marshuo.utils.MyBatisUtils;
import com.marshuo.utils.UUIDUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mars
 * @date 2022/09/20
 */
public class BlogMapperTest {
    @Test
    public void initBlog() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
    
            Blog blog = new Blog();
            blog.setId(UUIDUtils.getId());
            blog.setTitle("Java很简单");
            blog.setAuthor("张三");
            blog.setCreateTime(new Date());
            blog.setViews(123);
            mapper.addBlog(blog);
    
            blog.setId(UUIDUtils.getId());
            blog.setTitle("MyBatis很简单");
            blog.setAuthor("李四");
            blog.setCreateTime(new Date());
            blog.setViews(123);
            mapper.addBlog(blog);
    
            blog.setId(UUIDUtils.getId());
            blog.setTitle("微服务很简单");
            blog.setAuthor("王五");
            blog.setCreateTime(new Date());
            blog.setViews(123);
            mapper.addBlog(blog);
    
            blog.setId(UUIDUtils.getId());
            blog.setTitle("微服务很简单");
            blog.setAuthor("赵六");
            blog.setCreateTime(new Date());
            blog.setViews(123);
            mapper.addBlog(blog);
    
        }
        
    }
    
    @Test
    public void testAddBlog() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            mapper.addBlog(new Blog(UUIDUtils.getId(), "这是一个博客", "marshuo", new Date(), 15));
    
        }
    }
    
    @Test
    public void testGetBlogList() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            List<Blog> blogList = mapper.getBlogList();
            for (Blog blog : blogList) {
                System.out.println("blog = " + blog);
            }
        }
    }
    
    @Test
    public void testGetBlogsByIF() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
    
            Blog blog = new Blog();
            // 没有where条件
            List<Blog> blogs = mapper.getBlogsByIF(blog);
            for (Blog blog1 : blogs) {
                System.out.println("blog1 = " + blog1);
            }
            
            // where条件添加author
            blog.setAuthor("marshuo");
            blogs = mapper.getBlogsByIF(blog);
            for (Blog blog1 : blogs) {
                System.out.println("blog1 = " + blog1);
            }
    
            // where条件添加title
            blog.setTitle("Java很简单");
            blogs = mapper.getBlogsByIF(blog);
            for (Blog blog1 : blogs) {
                System.out.println("blog1 = " + blog1);
            }
            
        }
    }
    
    @Test
    public void testGetBlogsByChoose() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            Blog blog = new Blog();
            // 没有添加条件，查询到所有的记录
            List<Blog> blogList = mapper.getBlogsByChoose(blog);
            for (Blog blog1 : blogList) {
                System.out.println("blog1 = " + blog1);
            }
    
            // 根据title进行查询
            blog.setTitle("Java很简单");
            blogList = mapper.getBlogsByChoose(blog);
            for (Blog blog1 : blogList) {
                System.out.println("blog1 = " + blog1);
            }
    
            // 根据author进行查询，这个时候title条件不生效
            blog.setAuthor("marshuo");
            blogList = mapper.getBlogsByChoose(blog);
            for (Blog blog1 : blogList) {
                System.out.println("blog1 = " + blog1);
            }
            
            
        }
    }
    
    @Test
    public void testUpdateBlog() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
    
            Blog blog = new Blog();
            blog.setId("24e9f0cf72194edf99b1abcf37c30a25");
            // blog.setTitle("呵呵哈哈");
            // mapper.updateBlog(blog);
            
            blog.setTitle("摇摇晃晃");
            blog.setAuthor("布尔");
            mapper.updateBlog(blog);
        }
    }
    
    @Test
    public void testGetBlogsByForEach() {
        try (SqlSession sqlSession = MyBatisUtils.getSqlSession()) {
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
    
            List<String> list = new ArrayList<>();
            list.add("这是一个博客");
            list.add("摇摇晃晃");
    
            List<Blog> blogs = mapper.getBolgsByForEach(list);
            for (Blog blog : blogs) {
                System.out.println("blog = " + blog);
            }
        }
    }
}

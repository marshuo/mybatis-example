package com.marshuo.mapper;

import com.marshuo.entity.Blog;

import java.util.List;

/**
 * @author mars
 * @date 2022/09/24
 */
public interface BlogMapper {
    List<Blog> getBlogList();
    int addBlog(Blog blog);
    
    List<Blog> getBlogsByIF(Blog blog);
    
    List<Blog> getBlogsByChoose(Blog blog);
    
    int updateBlog(Blog blog);
    
    List<Blog> getBolgsByForEach(List<String> titles);
}

package com.marshuo.mapper;

import com.marshuo.entity.Blog;
import java.util.List;

public interface BlogMapper {
    int insert(Blog row);

    List<Blog> selectAll();
}
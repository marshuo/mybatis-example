package com.marshuo.mapper;

import com.marshuo.entity.Blog;
import com.marshuo.entity.BlogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BlogMapper {
    long countByExample(BlogExample example);

    int deleteByExample(BlogExample example);

    int insert(Blog row);

    int insertSelective(Blog row);

    List<Blog> selectByExample(BlogExample example);

    int updateByExampleSelective(@Param("row") Blog row, @Param("example") BlogExample example);

    int updateByExample(@Param("row") Blog row, @Param("example") BlogExample example);
}
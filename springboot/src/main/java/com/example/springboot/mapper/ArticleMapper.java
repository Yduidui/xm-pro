package com.example.springboot.mapper;


import com.example.springboot.entity.Article;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ArticleMapper {
    List<Article> selectAll(Article article);

//    @Select("select * from article where id = #{id}")  注解方式
    Article selectById(Integer id);

    void insert(Article article);

    void updateById(Article article);

    void deleteById(Integer id);

    @Select("select * from article where title = #{title}")
    Article selectByTitle(String title);

    @Select("select count(*) from article where time like'%${date}%'")
    Integer selectCountByDate(String date);
}

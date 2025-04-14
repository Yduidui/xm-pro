package com.example.springboot.mapper;


import com.example.springboot.entity.Admin;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdminMapper {
    List<Admin> selectAll(Admin admin);

//    @Select("select * from admin where id = #{id}")  注解方式
    Admin selectById(Integer id);

    void insert(Admin admin);

    void updateById(Admin admin);

    void deleteById(Integer id);

    @Select("select * from admin where username = #{username}")
    Admin selectByUsername(String username);
}

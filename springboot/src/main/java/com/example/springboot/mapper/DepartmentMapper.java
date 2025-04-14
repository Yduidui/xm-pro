package com.example.springboot.mapper;


import com.example.springboot.entity.Department;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DepartmentMapper {
    List<Department> selectAll(Department department);

//    @Select("select * from department where id = #{id}")  注解方式
    Department selectById(Integer id);

    void insert(Department department);

    void updateById(Department department);

    void deleteById(Integer id);

}

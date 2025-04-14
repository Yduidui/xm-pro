package com.example.springboot.mapper;


import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Employee;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmployeeMapper {
    List<Employee> selectAll(Employee employee);

//    @Select("select * from employee where id = #{id}")  注解方式
    Employee selectById(Integer id);

    void insert(Employee employee);

    void updateById(Employee employee);

    void deleteById(Integer id);

    @Select("select * from employee where username = #{username}")
    Employee selectByUsername(String username);

    @Select("select * from employee where jobnum = #{jobnum}")
    Employee selectByJobnum(String jobnum);

}

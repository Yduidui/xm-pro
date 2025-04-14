package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Department;
import com.example.springboot.entity.Employee;
import com.example.springboot.exception.CustomerException;
import com.example.springboot.mapper.DepartmentMapper;
import com.example.springboot.mapper.EmployeeMapper;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageInfo;

import java.util.List;

@Service
public class EmployeeService {

        @Resource
        private EmployeeMapper employeeMapper;
        @Resource
        private DepartmentMapper departmentMapper;

    public void add(Employee employee) {
        // ★ 部门名称 → 部门ID 映射逻辑 ★
        if (employee.getDepartmentId() == null && StrUtil.isNotBlank(employee.getDepartmentName())) {
            List<Department> departmentList = departmentMapper.selectAll(null);
            for (Department dept : departmentList) {
                if (employee.getDepartmentName().equals(dept.getName())) {
                    employee.setDepartmentId(dept.getId());
                    break;
                }
            }
        }

        // 检查用户名是否重复
        if (employeeMapper.selectByUsername(employee.getUsername()) != null) {
            throw new CustomerException("500", "账号重复，无法注册");
        }

        // 检查工号是否重复
        if (employeeMapper.selectByJobnum(employee.getJobnum()) != null) {
            throw new CustomerException("500", "工号重复，无法注册");
        }

        // 设置默认密码
        if (StrUtil.isBlank(employee.getPassword())) {
            employee.setPassword("123");
        }

        // 设置默认姓名（如果没填）
        if (StrUtil.isBlank(employee.getName())) {
            employee.setName(employee.getUsername());
        }

        // 设置默认角色为员工
        employee.setRole("EMP");

        // 插入数据库
        employeeMapper.insert(employee);
    }


    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    public void deleteById(Integer id) {
        employeeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }

    }
    public List<Employee> selectAll (Employee employee) {
        List<Employee> list = employeeMapper.selectAll(employee);
        return list;


    }

    public Employee selectById(Integer id) {
            Employee employee =  employeeMapper.selectById(id);
            return employee;
    }

    public PageInfo<Employee> selectPage(Employee employee, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Employee> list = employeeMapper.selectAll(employee);
            return PageInfo.of(list);
    }


    public Employee login(Account account) {
        String username = account.getUsername();
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if (dbEmployee == null) {
            throw new CustomerException("500", "账号不存在");
        }
        if (!dbEmployee.getPassword().equals(account.getPassword())) {
            throw new CustomerException("500", "账号或密码错误");
        }
        // 这里检查数据库的角色是否匹配
        if (!"EMP".equals(dbEmployee.getRole())) {
            throw new CustomerException("500", "身份错误，无法以普通员工身份登录");
        }
        return dbEmployee;
    }

    public void updatePassword(Account account) {
        // 打印一下接收到的数据
        System.out.println("Received Account: " + account);
        System.out.println("Received Account: " + account.getPassword());
        System.out.println("Received Account: " + account.getNewPassword());

        Integer id = account.getId();
        System.out.println("Received id: " + id);
        Employee employee = employeeMapper.selectById(id);
        System.out.println("Received employee: " + employee.getPassword());
        if(!employee.getPassword().equals(account.getPassword())) {
            throw new CustomerException("500", "原密码错误");
        }
        employee.setPassword(account.getNewPassword());
        //当前类的实例（当前 service），调用自己的 update 方法
        this.update(employee);
    }

    public void register(Employee employee) {
        this.add(employee);
    }
}

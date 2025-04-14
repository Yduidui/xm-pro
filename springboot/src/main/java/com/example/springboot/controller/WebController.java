package com.example.springboot.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Employee;
import com.example.springboot.exception.CustomerException;
import com.example.springboot.exception.GlobalExceptionHandler;
import com.example.springboot.service.AdminService;
import com.example.springboot.service.ArticleService;
import com.example.springboot.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@RestController
public class WebController {

    @Resource
    private EmployeeService employeeService;
    @Resource
    private AdminService adminService;
    @Resource
    private ArticleService articleService;

    @GetMapping("/hello")
    public Result hello(){
        return Result.success("hello");
    }

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account result = null;
        if ("ADMIN".equals(account.getRole())) {
            result = adminService.login(account);
        } else if ("EMP".equals(account.getRole())) {
            result = employeeService.login(account);
        } else {
            throw new CustomerException("500", "无效的角色");
        }

        if (result == null) {
            throw new CustomerException("500", "登录失败");
        }

        return Result.success(result);
    }


    @PostMapping("/register")
    public Result register(@RequestBody Employee employee){
        employeeService.register(employee);
        return Result.success();
    }
    /**
     * 新增数据
     *
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account){
        if ("ADMIN".equals(account.getRole())) {
            adminService.updatePassword(account);
        } else if ("EMP".equals(account.getRole())) {
            employeeService.updatePassword(account);
        } else {
            throw new CustomerException("500", "无效的角色");
        }

        return Result.success();
    }

    @GetMapping("/test")
    public Result test(){
        throw new RuntimeException("这是一个测试错误");
    }

    //主动抛异常
    @GetMapping("/calc")
    public Result calc(){
        throw new CustomerException("400","错误");
    }

    @GetMapping("/count")
    public Result count(){
        return Result.success(10);
    }

    @GetMapping("/barData")
    public Result barData() {

        //用字符串（String）作为“键”，用 List<Object> 作为“值”来存储数据。
        Map<String, List<Object>> map = new HashMap<>();

        // 获取所有员工列表 传 null 表示不带条件查询所有员工
        List<Employee> employeeList = employeeService.selectAll(null);

        // 获取所有部门名称并去重，作为 x 轴数据
        Set<String> departmentNameSet = employeeList.stream()  //把员工列表变成“流”。想象成一个流水线，每个员工对象会一个个传过去处理
                .map(Employee::getDepartmentName)  //对每个员工执行 getDepartmentName() 方法，提取他们的部门名
                .collect(Collectors.toSet());      //把这些部门名收集起来，并用 Set 存放。这样会自动去重，只保留不重复的部门名

        // x 轴：部门名称 把所有部门的名称（去重后的）放入 Map 的 "department" 键中
        map.put("department", new ArrayList<>(departmentNameSet));

        // y 轴：每个部门的员工数量
        List<Object> countList = new ArrayList<>();
        for (String departmentName : departmentNameSet) {
            long count = employeeList.stream()      //开始对员工列表进行流式操作。
                    .filter(employee -> departmentName.equals(employee.getDepartmentName())) //对每个员工进行判断： 只留下 部门名称 == 当前循环到的部门名 的员工。
                    .count();
            countList.add(count);
        }
        //把每个部门对应的员工人数放入 "count" 键中
        map.put("count", countList);

        return Result.success(map);
    }

    @GetMapping("/lineData")
    public Result lineData() {
        //用字符串（String）作为“键”，用 List<Object> 作为“值”来存储数据。
        Map<String, Object> map = new HashMap<>();

        Date date = new Date();
        DateTime start = DateUtil.offsetDay(date, -7); //起始日期
        List<DateTime> dateTimeList =  DateUtil.rangeToList(start, date, DateField.DAY_OF_YEAR);
        //把DateTime类型的日期转换为 字符串类型的日期
        List<String> dateStrList =  dateTimeList.stream().map(dateTime -> DateUtil.format(dateTime, "MM月dd日"))
                .sorted(Comparator.naturalOrder()).collect(Collectors.toList());

        List<Integer> countList = new ArrayList<>();
        map.put("date", dateStrList);

        for (Date day : dateTimeList) {
            String dayFormat =  DateUtil.formatDate(day);
            Integer count =  articleService.selectCountByDate(dayFormat);
            countList.add(count);
        }
        map.put("count", countList);        //y轴发布文章的数量

        return Result.success(map);
    }

    @GetMapping("/pieData")
    public Result pieData() {
       List<Map<String, Object>> list = new ArrayList<>();
        // 获取所有员工列表 传 null 表示不带条件查询所有员工
        List<Employee> employeeList = employeeService.selectAll(null);
        // 获取所有部门名称并去重，作为 x 轴数据
        Set<String> departmentNameSet = employeeList.stream()  //把员工列表变成“流”。想象成一个流水线，每个员工对象会一个个传过去处理
                .map(Employee::getDepartmentName)  //对每个员工执行 getDepartmentName() 方法，提取他们的部门名
                .collect(Collectors.toSet());      //把这些部门名收集起来，并用 Set 存放。这样会自动去重，只保留不重复的部门名
        for (String departmentName : departmentNameSet) {
            HashMap<String, Object> map =  new HashMap<>();
            map.put("name", departmentName);
            long count = employeeList.stream()      //开始对员工列表进行流式操作。
                    .filter(employee -> departmentName.equals(employee.getDepartmentName())) //对每个员工进行判断： 只留下 部门名称 == 当前循环到的部门名 的员工。
                    .count();
            map.put("value", count);

            list.add(map);
        }

        return Result.success(list);
    }


}

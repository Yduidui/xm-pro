package com.example.springboot.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Employee;
import com.example.springboot.service.EmployeeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

    /**
     * 新增数据
     *
     */
    @PostMapping("/add")
    public Result add(@RequestBody Employee employee){  //将前端的json映射为Java的对象
        employeeService.add(employee);
        return Result.success();
    }

    /**
    * 更新数据
    */
    @PutMapping("/update")
    public Result update(@RequestBody Employee employee){
        employeeService.update(employee);
        return Result.success(employee);
    }

    /**
     * 删除数据
     */
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        employeeService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除数据
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        employeeService.deleteBatch(ids);
        return Result.success();
    }


    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Employee employee){
        List<Employee> list = employeeService.selectAll(employee);
        return Result.success(list);
    }

    /**
     * 查询单个
     * @param id
     * @return
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    /**
     * 分页查询
     * pageNum 当前页码
     * pageSize 每页个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(Employee employee,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Employee> pageInfo = employeeService.selectPage(employee,pageNum, pageSize);
        return Result.success(pageInfo);

    }


    /**
     * 导出excel
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        //1.拿到所有员工数据
        List<Employee> employeeList =  employeeService.selectAll(null);
        //2.构建ExcelWriter
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //3.设置中文表头
        writer.addHeaderAlias("username","账号");
        writer.addHeaderAlias("name","姓名");
        writer.addHeaderAlias("sex","性别");
        writer.addHeaderAlias("jobnum","工号");
        writer.addHeaderAlias("age","年龄");
        writer.addHeaderAlias("descr","个人简介");
        writer.addHeaderAlias("departmentName","部门");
        //默认的，未添加alias的属性也会写出，相只写出加了别名的字段，使用这个方法排除
        writer.setOnlyAlias(true);
        //4.写出数据
        writer.write(employeeList, true);
        //5.设置输出文件名称 以及输出流的头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("员工信息", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        //6.写出输出流 并关闭
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
    }

    /**
     * 导入excel
     */
    @PostMapping("/import")
    public Result importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        reader.addHeaderAlias("账号","username");
        reader.addHeaderAlias("姓名","name");
        reader.addHeaderAlias("性别","sex");
        reader.addHeaderAlias("工号","jobnum");
        reader.addHeaderAlias("年龄","age");
        reader.addHeaderAlias("个人简介","descr");
        reader.addHeaderAlias("部门","departmentName");

        List<Employee> employeeList = reader.readAll(Employee.class);
        System.out.println("读取到的数据条数：" + employeeList.size());

        for (Employee employee : employeeList) {
            try {
                employeeService.add(employee);
                System.out.println("✅ 插入成功：" + employee.getUsername());
            } catch (Exception e) {
                System.out.println("❌ 插入失败：" + employee.getUsername() + "，原因：" + e.getMessage());
                e.printStackTrace(); // 打印具体异常
            }
        }

        return Result.success();
    }

//    /**
//     *
//     * url传参
//     */
//    @GetMapping("/selectOne")
//    public Result selectOne(@RequestParam Integer id, @RequestParam String name){
//        Employee employee = employeeService.selectById(id);
//        return Result.success(employee);
//    }
}

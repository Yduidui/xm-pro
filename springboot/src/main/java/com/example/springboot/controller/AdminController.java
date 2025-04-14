package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Admin;
import com.example.springboot.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    /**
     * 新增数据
     *
     */
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin){  //将前端的json映射为Java的对象
        adminService.add(admin);
        return Result.success();
    }

    /**
    * 更新数据
    */
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin){
        adminService.update(admin);
        return Result.success(admin);
    }

    /**
     * 删除数据
     */
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id){
        adminService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除数据
     */
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        adminService.deleteBatch(ids);
        return Result.success();
    }


    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Admin admin){
        List<Admin> list = adminService.selectAll(admin);
        return Result.success(list);
    }

    /**
     * 查询单个
     * @param id
     * @return
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }

    /**
     * 分页查询
     * pageNum 当前页码
     * pageSize 每页个数
     */
    @GetMapping("/selectPage")
    public Result selectPage(Admin admin,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Admin> pageInfo = adminService.selectPage(admin,pageNum, pageSize);
        return Result.success(pageInfo);

    }

//    /**
//     *
//     * url传参
//     */
//    @GetMapping("/selectOne")
//    public Result selectOne(@RequestParam Integer id, @RequestParam String name){
//        Admin admin = adminService.selectById(id);
//        return Result.success(admin);
//    }
}

package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.exception.CustomerException;
import com.example.springboot.mapper.AdminMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

        @Resource
        private AdminMapper adminMapper;

    public void add(Admin admin) {
        String username = admin.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if(dbAdmin!= null) {
            throw new CustomerException("500", "账号重复,无法注册");
        }
        if (StrUtil.isBlank(admin.getPassword())) {  //密码没填
            admin.setPassword("admin");        //默认密码admin
        }
        if (StrUtil.isBlank(admin.getName())) {  //密码没填
            admin.setName(admin.getUsername());        //默认名称
        }
        //设置角色
        admin.setRole("ADMIN");        //管理员角色
        adminMapper.insert(admin);
    }

    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }

    }
    public List<Admin> selectAll (Admin admin) {
        List<Admin> list = adminMapper.selectAll(admin);
        return list;


    }

    public Admin selectById(Integer id) {
            Admin admin =  adminMapper.selectById(id);
            return admin;
    }

    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
            return PageInfo.of(list);
    }


    public Admin login(Account account) {
        String username = account.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin == null) {
            throw new CustomerException("500", "账号不存在");
        }
        if (!dbAdmin.getPassword().equals(account.getPassword())) {
            throw new CustomerException("500", "账号或密码错误");
        }
        // 这里检查数据库的角色是否匹配
        if (!"ADMIN".equals(dbAdmin.getRole())) {
            throw new CustomerException("500", "身份错误，无法以管理员身份登录");
        }
        return dbAdmin;
    }


    public void updatePassword(Account account) {
        Integer id = account.getId();
        Admin admin = adminMapper.selectById(id);
        if(!admin.getPassword().equals(account.getPassword())) {
            throw new CustomerException("500", "原密码错误");
        }
        admin.setPassword(account.getNewPassword());
        //当前类的实例（当前 service），调用自己的 update 方法
        this.update(admin);
    }
}

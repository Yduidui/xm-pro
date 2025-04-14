package com.example.springboot.entity;

public class Employee extends Account{
    private Integer id;            // 员工ID
    private String name;           // 姓名
    private String sex;            // 性别
    private String jobnum;         // 工号
    private String descr;          // 描述
    private Integer age;           // 年龄
    private Integer departmentId;  // 部门ID
    private String password;
    private String username;
    private String role;
    private String departmentName;


    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // 构造函数
    public Employee() {
    }

    public Employee(Integer id, String name, String sex, String jobnum, String descr, Integer age, Integer departmentId) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.jobnum = jobnum;
        this.descr = descr;
        this.age = age;
        this.departmentId = departmentId;
    }

    // Getter 和 Setter 方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getJobnum() {
        return jobnum;
    }

    public void setJobnum(String jobnum) {
        this.jobnum = jobnum;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", jobnum='" + jobnum + '\'' +
                ", descr='" + descr + '\'' +
                ", age=" + age +
                ", departmentId=" + departmentId +
                '}';
    }
}
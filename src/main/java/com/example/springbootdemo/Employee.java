package com.example.springbootdemo;

import java.time.LocalDate;

public class Employee {
    private Integer id;
    private String name;
    private Integer age;
    private String gender;
    private LocalDate entryDate; // 自动对应数据库的 entry_date

    // === 下面是标准的 Getter 和 Setter 方法（快捷键 Alt+Insert 可以自动生成） ===
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }
}

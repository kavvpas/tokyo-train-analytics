package com.example.springbootdemo;

public class Student {
    private Integer id;
    private String studentNumber; // 🔍 注意：数据库里叫 student_number，Java里用标准的驼峰命名法 studentNumber
    private String name;
    private Integer age;
    private String gender;
    private Integer score;

    // ==================== 下面是标准老规矩：Getter / Setter ====================
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }
}
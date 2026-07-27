package com.example.springbootdemo;

import java.util.List;

public interface StudentService {
    // 业务声明：获取所有学生列表
    List<Student> getAllStudents();
}
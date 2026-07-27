package com.example.springbootdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // 👈 昨晚的大坑！死死贴住这顶帽子，Spring 才会认它！
public class StudentServiceImpl implements StudentService {

    @Autowired // 👈 把我们刚刚写好的打手注入进来
    private StudentMapper studentMapper;

    @Override
    public List<Student> getAllStudents() {
        // 军师自己不干体力活，直接转发给真打手
        return studentMapper.getAllStudents();
    }
}

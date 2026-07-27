package com.example.springbootdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // 👈 圣旨！告诉 Spring Boot：老子是核心的业务层（Service），必须在后台帮我 new 出来！
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired // 军师调用“真打手”去敲数据库的门
    private EmployeeMapper employeeMapper;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeMapper.getAllEmployees();
    }

    @Override
    public int insertEmployee(Employee employee) {
        // 核心转发：这里调用的是唯一的真打手 employeeMapper！
        return employeeMapper.insertEmployee(employee);
    }

    @Override
    public int deleteEmployeeById(Integer id) {
        return employeeMapper.deleteEmployeeById(id);
    }

    @Override
    public int updateEmployee(Employee employee) {
        return employeeMapper.updateEmployee(employee);
    }
}

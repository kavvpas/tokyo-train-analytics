package com.example.springbootdemo;

import java.util.List;

public interface EmployeeService {
    // 定义一个“获取所有员工”的业务功能
    List<Employee> getAllEmployees();
    // 2. 新增员工（把这一行补进去，红线就消了！）
    int insertEmployee(Employee employee);

    // 3. 根据 ID 删除员工
    int deleteEmployeeById(Integer id);

    // 4. 修改员工
    int updateEmployee(Employee employee);
}

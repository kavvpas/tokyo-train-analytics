package com.example.springbootdemo;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface EmployeeMapper {

    // 1. 查询所有
    @Select("SELECT id, name, age, gender, entry_date FROM employee")
    List<Employee> getAllEmployees();

    // 2. 新增员工
    @Insert("INSERT INTO employee(name, age, gender, entry_date) VALUES(#{name}, #{age}, #{gender}, #{entryDate})")
    int insertEmployee(Employee employee);

    // 3. 根据 ID 删除员工（把这一招补给它，delete 红线立刻消失！）
    @Delete("DELETE FROM employee WHERE id = #{id}")
    int deleteEmployeeById(Integer id);

    // 4. 修改员工信息（把这一招补给它，update 红线立刻消失！）
    @Update("UPDATE employee SET name = #{name}, age = #{age} WHERE id = #{id}")
    int updateEmployee(Employee employee);
}

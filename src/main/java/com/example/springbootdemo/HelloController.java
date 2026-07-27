package com.example.springbootdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class HelloController {

    // 🟢 简单粗暴：直接注入打手 Mapper，咱们不找军师了！
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired private EmployeeService employeeService;

    // ==================== 1. 查询全部员工 ====================
    @RequestMapping("/empList")
    public List<Employee> getEmpList() {
        return employeeMapper.getAllEmployees();
    }

    // ==================== 2. 新增员工（企业级 JSON 大礼包版） ====================
    // 现在不需要一堆 @RequestParam 了，我们一个 @RequestBody 直接打包带走！
    // 访问路径将变得极其干净：http://localhost:8080/emp/add
    @RequestMapping("/emp/add")
    public String addEmployee(@RequestBody Employee emp) {

        emp.setEntryDate(LocalDate.now());

        // 🟢 确保这里开头是小写的 e ！！！
        int rows = employeeService.insertEmployee(emp);

        if (rows > 0) {
            return "🎉 【企业级 JSON 时代】开启！新员工【" + emp.getName() + "】成功上船！";
        } else {
            return "❌ 入团申请被拒绝！";
        }
    }

    // ==================== 3. 根据 ID 删除员工 ====================
    @RequestMapping("/emp/delete/{id}")
    public String deleteEmployee(@PathVariable Integer id) {
        int rows = employeeMapper.deleteEmployeeById(id);

        if (rows > 0) {
            return "👋 成功！ID 为 " + id + " 的员工已被无情开除！";
        } else {
            return "❓ 查无此人。";
        }
    }

    // ==================== 4. 修改员工信息 ====================
    @RequestMapping("/emp/update")
    public String updateEmployee(
            @RequestParam Integer id,
            @RequestParam String name,
            @RequestParam Integer age
    ) {
        Employee emp = new Employee();
        emp.setId(id);
        emp.setName(name);
        emp.setAge(age);

        int rows = employeeMapper.updateEmployee(emp);

        if (rows > 0) {
            return "⚡ 悬赏令更新！ID 为 " + id + " 的员工资料已成功修改！";
        } else {
            return "❌ 修改失败！";
        }
    }

    // ==================== 5. 之前的经典测试招式（保留） ====================
    @RequestMapping("/hello")
    public String sayHello() {
        return "<h1>Wc！我的第一个 Spring Boot 网页居然成功了！</h1>";
    }

    @RequestMapping("/order")
    public String getOrder(@RequestParam String id, @RequestParam String product) {
        return "收到订单！订单编号: " + id + "，购买商品: " + product;
    }

    @RequestMapping("/player/{name}")
    public String getPlayer(@PathVariable String name) {
        return "当前正在查看的角色皮肤是: " + name;
    }
}
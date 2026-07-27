package com.example.springbootdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController // 👈 告诉系统，我是专门通过网络返回数据的接待前台
public class MacOrderController {

    @Autowired
    private MacOrderService macOrderService; // 👈 注入小写的学生军师真道具

    // 访问路径：http://localhost:8080/stuList
    @RequestMapping("/order/place")
    public List<MacOrder> getMacOrderList() {
        // 直接让军师去数据库里捞一波
        return macOrderService.getAllmacOrders();
    }
}

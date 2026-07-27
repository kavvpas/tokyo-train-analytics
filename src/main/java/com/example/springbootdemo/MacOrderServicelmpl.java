package com.example.springbootdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // 👈 昨晚的大坑！死死贴住这顶帽子，Spring 才会认它！
public class MacOrderServicelmpl implements MacOrderService {

    @Autowired // 👈 把我们刚刚写好的打手注入进来
    private MacOrderMapper macOrderMapper;

    @Override
    public List<MacOrder> getAllmacOrders() {
        return macOrderMapper.getAllmacOrder();
    }


}

package com.example.springbootdemo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper // 👈 圣旨：告诉 Spring Boot，我是直接跟数据库对砍的打手！
public interface MacOrderMapper {

    // 🎯 核心招式：查询 student 表里的所有倒霉蛋
    @Select("SELECT id, order_number, food_name, quantity, remark FROM macOrder")
    List<MacOrder> getAllmacOrder();
}

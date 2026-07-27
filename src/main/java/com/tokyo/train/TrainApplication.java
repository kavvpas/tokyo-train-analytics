package com.tokyo.train;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 🌟 核心魔法注解：告诉 Java，老子现在是正规的 Spring Boot 微服务项目了！
@SpringBootApplication
public class TrainApplication {
    public static void main(String[] args) {
        // 啪的一下，点火启动！它会自动在后台拉起一个 8080 端口的 Web 服务器
        SpringApplication.run(TrainApplication.class, args);
    }
}

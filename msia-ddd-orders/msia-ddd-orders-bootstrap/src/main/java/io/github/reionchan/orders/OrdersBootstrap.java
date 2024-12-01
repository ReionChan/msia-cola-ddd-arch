package io.github.reionchan.orders;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * 订单服务启动器
 *
 * @author Reion
 * @date 2023-12-12
 **/
@EnableMethodSecurity
@MapperScan(basePackages = "io.github.reionchan.*.database.mapper")
@EnableFeignClients(basePackages = {"io.github.reionchan.core.rpc.feign"})
@SpringBootApplication(scanBasePackages = {"io.github.reionchan", "com.alibaba.cola"})
public class OrdersBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(OrdersBootstrap.class, args);
    }
}

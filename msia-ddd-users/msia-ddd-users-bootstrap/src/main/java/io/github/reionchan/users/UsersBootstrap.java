package io.github.reionchan.users;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * 用户服务启动器
 *
 * @author Reion
 * @date 2023-12-12
 **/
@SpringBootApplication(scanBasePackages = {"io.github.reionchan", "com.alibaba.cola"})
@EnableMethodSecurity
@MapperScan(basePackages = "io.github.reionchan.*.database.mapper")
@EnableFeignClients(basePackages = {"io.github.reionchan.core.rpc.feign"})
public class UsersBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(UsersBootstrap.class, args);
    }
}

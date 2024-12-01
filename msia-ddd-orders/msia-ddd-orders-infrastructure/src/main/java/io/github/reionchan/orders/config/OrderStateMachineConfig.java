package io.github.reionchan.orders.config;

import io.github.reionchan.orders.event.OrderStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Reion
 * @date 2024-07-05
 **/
@Slf4j
@Configuration
public class OrderStateMachineConfig {

    @Bean
    public OrderStateMachine userStateMachine() {
        return new OrderStateMachine();
    }
}

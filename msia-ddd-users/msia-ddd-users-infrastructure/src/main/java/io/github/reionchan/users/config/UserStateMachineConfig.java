package io.github.reionchan.users.config;

import io.github.reionchan.users.event.UserStateMachine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Reion
 * @date 2024-07-05
 **/
@Slf4j
@Configuration
public class UserStateMachineConfig {

    @Bean
    public UserStateMachine userStateMachine() {
        return new UserStateMachine();
    }
}

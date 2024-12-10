package io.github.reionchan.users.config;

import io.github.reionchan.core.mq.MQManager;
import io.github.reionchan.users.dto.UserDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

/**
 * @author Reion
 * @date 2024-07-05
 **/
@Slf4j
@Configuration
public class UserMQConfig {

    @Resource
    private MQManager mqManager;

    @Bean
    public Consumer<Message<UserDTO>> userRegisterAck() {
        return msg -> {
            mqManager.ackMessage(msg);
        };
    }
}

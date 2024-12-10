package io.github.reionchan.products.config;

import io.github.reionchan.core.mq.MQManager;
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
public class StockMQConfig {
    @Resource
    private MQManager mqManager;

    @Bean
    public Consumer<Message<String>> stockSubAck() {
        return msg -> {
            mqManager.ackMessage(msg);
        };
    }
}

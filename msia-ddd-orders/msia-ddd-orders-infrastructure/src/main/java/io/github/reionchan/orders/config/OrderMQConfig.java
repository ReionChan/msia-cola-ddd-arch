package io.github.reionchan.orders.config;

import io.github.reionchan.core.mq.MQManager;
import io.github.reionchan.products.dto.StockDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Reion
 * @date 2024-07-05
 **/
@Slf4j
@Configuration
public class OrderMQConfig {
    @Resource
    private MQManager mqManager;

    @Bean
    public Consumer<Message<Map<Long, Set<StockDTO>>>> orderPayAck() {
        return msg -> {
            mqManager.ackMessage(msg);
        };
    }

}

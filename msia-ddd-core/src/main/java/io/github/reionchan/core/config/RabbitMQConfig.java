package io.github.reionchan.core.config;

import io.github.reionchan.core.mq.MQManager;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.support.ErrorMessage;

/**
 * @author Reion
 * @date 2024-12-07
 **/
@Slf4j
@Configuration
public class RabbitMQConfig {

    @Resource
    private MQManager mqManager;

    @ServiceActivator(inputChannel = "errorChannel")
    public void errorChannelLogger(ErrorMessage errorMessage) {
        mqManager.errorMessage(errorMessage);
    }
}

package io.github.reionchan.mq.config;

import io.github.reionchan.mq.callback.SimpleMQCallback;
import io.github.reionchan.mq.repository.IMQRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Reion
 * @date 2024-07-08
 **/
@Slf4j
@Configuration
public class MQProducerConfig {

    @Bean
    @ConditionalOnMissingBean(RabbitTemplate.ConfirmCallback.class)
    public RabbitTemplate.ConfirmCallback confirmCallback(IMQRepository mqRepository) {
        return new SimpleMQCallback(mqRepository);
    }

    @Bean
    @ConditionalOnMissingBean(RabbitTemplate.ReturnsCallback.class)
    public RabbitTemplate.ReturnsCallback returnsCallback(IMQRepository mqRepository) {
        return new SimpleMQCallback(mqRepository);
    }

    @Bean
    @ConditionalOnMissingBean(RabbitTemplateCustomizer.class)
    public RabbitTemplateCustomizer templateCustomizer(RabbitTemplate.ConfirmCallback confirmCallback, RabbitTemplate.ReturnsCallback returnsCallback) {
        return rabbitTemplate -> {
            log.info("--- 生产方配置 RabbitTemplate 可靠消息发送 ---");
            rabbitTemplate.setMandatory(true);
            rabbitTemplate.setConfirmCallback(confirmCallback);
            rabbitTemplate.setReturnsCallback(returnsCallback);
        };
    }
}
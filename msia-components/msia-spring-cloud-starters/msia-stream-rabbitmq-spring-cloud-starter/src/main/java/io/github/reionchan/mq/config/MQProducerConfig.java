package io.github.reionchan.mq.config;

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
    public RabbitTemplate.ConfirmCallback confirmCallback() {
        return (correlationData, ack, cause) -> {
            log.info("correlationData: {} - ack: {} - cause: {}", correlationData.getId(), ack, cause);
        };
    }

    @Bean
    @ConditionalOnMissingBean(RabbitTemplate.ReturnsCallback.class)
    public RabbitTemplate.ReturnsCallback returnsCallback() {
        return returned -> {
            log.info("returnedMessage: {}", returned);
        };
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

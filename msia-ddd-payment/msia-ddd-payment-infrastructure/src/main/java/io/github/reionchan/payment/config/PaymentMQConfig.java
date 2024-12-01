package io.github.reionchan.payment.config;

import io.github.reionchan.payment.consts.RabbitMQConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Reion
 * @date 2024-07-05
 **/
@Slf4j
@Configuration
public class PaymentMQConfig {
    @Bean
    public DirectExchange stockSubDirectExchange() {
        return new DirectExchange(RabbitMQConst.STOCK_SUB_EXCHANGE);
    }

    @Bean
    public DirectExchange stockSubDirectDLX() {
        return new DirectExchange(RabbitMQConst.STOCK_SUB_DLX);
    }

    @Bean
    public Queue stockSubQueue() {
        return QueueBuilder.durable(RabbitMQConst.STOCK_SUB_QUEUE).deadLetterExchange(RabbitMQConst.STOCK_SUB_DLX).deadLetterRoutingKey(RabbitMQConst.STOCK_SUB_DLX_ROUTING_KEY).build();
    }

    @Bean
    public Queue stockSubDlq() {
        return new Queue(RabbitMQConst.STOCK_SUB_DLQ);
    }

    @Bean
    public Binding stockSubBinding() {
        return BindingBuilder.bind(stockSubQueue()).to(stockSubDirectExchange()).with(RabbitMQConst.STOCK_SUB_ROUTING_KEY);
    }

    @Bean
    public Binding stockSubDlqBinding() {
        return BindingBuilder.bind(stockSubDlq()).to(stockSubDirectDLX()).with(RabbitMQConst.STOCK_SUB_DLX_ROUTING_KEY);
    }
}

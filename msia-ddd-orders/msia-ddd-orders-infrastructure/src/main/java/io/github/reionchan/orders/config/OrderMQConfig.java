package io.github.reionchan.orders.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.reionchan.orders.consts.RabbitMQConst.*;

/**
 * @author Reion
 * @date 2024-07-05
 **/
@Slf4j
@Configuration
public class OrderMQConfig {

    @Bean
    public DirectExchange orderDirectExchange() {
        return new DirectExchange(ORDER_PAY_EXCHANGE);
    }

    @Bean
    public DirectExchange directDLX() {
        return new DirectExchange(ORDER_PAY_DLX);
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_PAY_QUEUE).deadLetterExchange(ORDER_PAY_DLX).deadLetterRoutingKey(ORDER_PAY_DLX_ROUTING_KEY).build();
    }

    @Bean
    public Queue dlq() {
        return new Queue(ORDER_PAY_DLQ);
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderDirectExchange()).with(ORDER_PAY_ROUTING_KEY);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlq()).to(directDLX()).with(ORDER_PAY_DLX_ROUTING_KEY);
    }
}

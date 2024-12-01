package io.github.reionchan.products.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.reionchan.orders.consts.RabbitMQConst.*;
import static io.github.reionchan.products.consts.RabbitMQConst.*;


/**
 * @author Reion
 * @date 2024-07-05
 **/
@Slf4j
@Configuration
public class StockMQConfig {

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

    @Bean
    public DirectExchange stockSubDirectExchange() {
        return new DirectExchange(STOCK_SUB_EXCHANGE);
    }

    @Bean
    public DirectExchange stockSubDirectDLX() {
        return new DirectExchange(STOCK_SUB_DLX);
    }

    @Bean
    public Queue stockSubQueue() {
        return QueueBuilder.durable(STOCK_SUB_QUEUE).deadLetterExchange(STOCK_SUB_DLX).deadLetterRoutingKey(STOCK_SUB_DLX_ROUTING_KEY).build();
    }

    @Bean
    public Queue stockSubDlq() {
        return new Queue(STOCK_SUB_DLQ);
    }

    @Bean
    public Binding stockSubBinding() {
        return BindingBuilder.bind(stockSubQueue()).to(stockSubDirectExchange()).with(STOCK_SUB_ROUTING_KEY);
    }

    @Bean
    public Binding stockSubDlqBinding() {
        return BindingBuilder.bind(stockSubDlq()).to(stockSubDirectDLX()).with(STOCK_SUB_DLX_ROUTING_KEY);
    }
}

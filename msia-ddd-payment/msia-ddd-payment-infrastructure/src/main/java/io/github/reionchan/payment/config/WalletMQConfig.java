package io.github.reionchan.payment.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.reionchan.users.consts.RabbitMQConst.*;


/**
 * @author Reion
 * @date 2024-07-05
 **/
@Slf4j
@Configuration
public class WalletMQConfig {

    @Bean
    public DirectExchange orderDirectExchange() {
        return new DirectExchange(USER_REGISTER_EXCHANGE);
    }

    @Bean
    public DirectExchange directDLX() {
        return new DirectExchange(USER_REGISTER_DLX);
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(USER_REGISTER_QUEUE).deadLetterExchange(USER_REGISTER_DLX).deadLetterRoutingKey(USER_REGISTER_DLX_ROUTING_KEY).build();
    }

    @Bean
    public Queue dlq() {
        return new Queue(USER_REGISTER_DLQ);
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderDirectExchange()).with(USER_REGISTER_ROUTING_KEY);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlq()).to(directDLX()).with(USER_REGISTER_DLX_ROUTING_KEY);
    }
}

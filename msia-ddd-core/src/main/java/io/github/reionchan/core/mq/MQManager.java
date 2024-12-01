package io.github.reionchan.core.mq;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.RejectPolicy;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import io.github.reionchan.mq.model.entity.MQMessage;
import io.github.reionchan.mq.repository.IMQRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Reion
 * @date 2024-11-22
 **/
@Slf4j
@Component
public class MQManager {

    private static ExecutorService mqExecutor = ExecutorBuilder.create()
            .setCorePoolSize(Runtime.getRuntime().availableProcessors())
            .setMaxPoolSize(32)
            .setWorkQueue(new LinkedBlockingDeque<>(1024))
            .setHandler(RejectPolicy.BLOCK.getValue())
            .setThreadFactory(ThreadFactoryBuilder.create().setNamePrefix("mq-").build())
            .build();

    @Resource
    private IMQRepository mqRepository;
    @Resource
    private RabbitTemplate rabbitTemplate;

    public boolean save(MQMessage registerMsg) {
        return mqRepository.save(registerMsg);
    }

    public boolean Update(MQMessage message) {
        return mqRepository.updateById(message);
    }

    public void sendMessage(String exchange, String routingKey,  MQMessage msg, CorrelationData correlationData) {
        CompletableFuture.runAsync(() -> {
            try {
                Message message = MessageBuilder.withBody(msg.getContent().getBytes())
                        .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                        .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                        .build();
                rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
            } catch (Exception e) {
                log.error("异步发送消息异常", e);
            }
        }, mqExecutor);
    }
}

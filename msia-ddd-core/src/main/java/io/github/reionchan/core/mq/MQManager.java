package io.github.reionchan.core.mq;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.RejectPolicy;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.alibaba.fastjson2.JSON;
import io.github.reionchan.core.consts.MessageStatus;
import io.github.reionchan.core.model.entity.MQMessage;
import io.github.reionchan.core.repository.IMQRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.amqp.support.ReturnedAmqpMessageException;
import org.springframework.integration.support.MutableMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

import static cn.hutool.core.util.ObjectUtil.isNotEmpty;
import static org.springframework.amqp.support.AmqpHeaders.*;

/**
 * @author Reion
 * @date 2024-11-22
 **/
@Slf4j
@Component
public class MQManager {

    private static ExecutorService mqExecutor = ExecutorBuilder.create()
            .setCorePoolSize(Runtime.getRuntime().availableProcessors())
            .setMaxPoolSize(64)
            .setWorkQueue(new LinkedBlockingDeque<>(2048))
            .setHandler(RejectPolicy.BLOCK.getValue())
            .setThreadFactory(ThreadFactoryBuilder.create().setNamePrefix("mq-").build())
            .build();

    @Resource
    private IMQRepository mqRepository;
    @Resource
    private StreamBridge streamBridge;

    public boolean save(MQMessage registerMsg) {
        return mqRepository.save(registerMsg);
    }

    public boolean Update(MQMessage message) {
        return mqRepository.updateById(message);
    }

    public <T> void sendMessage(String exchange, String routingKey, MQMessage msg, Class<T> clazz, CorrelationData correlationData) {
        CompletableFuture.runAsync(() -> {
            try {
                Message<T> m = new MutableMessage<>(JSON.parseObject(msg.getContent(), clazz));
                // 消息确认-关联ID
                m.getHeaders().put(CORRELATION_ID, correlationData.getId());
                // SpringCloud Stream 包装 org.springframework.amqp.rabbit.core.RabbitTemplate.setupConfirm
                m.getHeaders().put(PUBLISH_CONFIRM_CORRELATION, correlationData);
                // DefaultAmqpHeaderMapper.populateStandardHeaders
                m.getHeaders().put(MESSAGE_ID, msg.getMessageId());
                streamBridge.send(exchange, m);
            } catch (Exception e) {
                log.error("异步发送消息异常", e);
            }
        }, mqExecutor);
    }
    public void ackMessage(Message<?> msg) {
        CompletableFuture.runAsync(() -> {
            try {
                boolean ack = (boolean) msg.getHeaders().get(PUBLISH_CONFIRM);
                CorrelationData correlationData = (CorrelationData) msg.getHeaders().get(PUBLISH_CONFIRM_CORRELATION);
                String cause = (String) msg.getHeaders().get(PUBLISH_CONFIRM_NACK_CAUSE);
                String msgId = correlationData.getId();

                MQMessage dbMessage = mqRepository.getById(msgId.replaceAll("-", ""));
                Assert.isTrue(isNotEmpty(dbMessage), msgId + " 未知的消息");
                if (!ack) {
                    log.warn("ACK INFO:\nid: {}\nack: {}\ncause: {}\nmsg: {}", msgId, ack, cause, msg);
                    dbMessage.setMessageStatus(MessageStatus.SENT_ERROR.getValue());
                    dbMessage.setRemark(cause);
                } else {
                    log.info("ACK INFO:\nid: {}\nack: {}\ncause: {}\nmsg: {}", msgId, ack, cause, msg);
                    dbMessage.setMessageStatus(MessageStatus.SENT.getValue());
                    dbMessage.setRemark("");
                }
                mqRepository.updateById(dbMessage);
            } catch (Exception e) {
                log.error("消息确认异常", e);
            }
        }, mqExecutor);
    }

    public void errorMessage(ErrorMessage errorMessage) {
        CompletableFuture.runAsync(() -> {
            try {
                Throwable error = errorMessage.getPayload();
                if (error instanceof ReturnedAmqpMessageException rame) {
                    MessageProperties properties = rame.getAmqpMessage().getMessageProperties();
                    String msgId = properties.getCorrelationId();
                    log.warn("RETURN INFO:\nid: {}\nexchange: {}\nroutingKey: {}\nreplyCode: {}\nreplyText: {}",
                            msgId, rame.getExchange(), rame.getRoutingKey(), rame.getReplyCode(), rame.getReplyText());
                    MQMessage dbMessage = mqRepository.getById(msgId.replaceAll("-", ""));
                    Assert.isTrue(isNotEmpty(dbMessage), msgId + " 未知的消息");
                    dbMessage.setMessageStatus(MessageStatus.DELIVER_ERROR.getValue());
                    dbMessage.setRemark(rame.getReplyCode() + "-" + rame.getReplyText());
                    mqRepository.updateById(dbMessage);
                    return;
                }
                log.warn("ERROR INFO:\n{}", error);
            } catch (Exception e) {
                log.error("异常消息记录错误", e);
            }
        }, mqExecutor);
    }
}

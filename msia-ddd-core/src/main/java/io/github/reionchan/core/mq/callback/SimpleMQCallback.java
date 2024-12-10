package io.github.reionchan.core.mq.callback;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.RejectPolicy;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import io.github.reionchan.core.consts.MessageStatus;
import io.github.reionchan.core.model.entity.MQMessage;
import io.github.reionchan.core.repository.IMQRepository;
import io.github.reionchan.mq.consts.RabbitMQConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

import static cn.hutool.core.util.ObjectUtil.isEmpty;
import static cn.hutool.core.util.ObjectUtil.isNotEmpty;
import static org.springframework.amqp.rabbit.connection.PublisherCallbackChannel.RETURNED_MESSAGE_CORRELATION_KEY;


/**
 * @author Reion
 * @date 2024-07-05
 **/
@Slf4j
public class SimpleMQCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    private static ExecutorService callbackExecutor = ExecutorBuilder.create()
            .setCorePoolSize(Runtime.getRuntime().availableProcessors())
            .setMaxPoolSize(32)
            .setWorkQueue(new LinkedBlockingDeque<>(1024))
            .setHandler(RejectPolicy.BLOCK.getValue())
            .setThreadFactory(ThreadFactoryBuilder.create().setNamePrefix("mq-callback-").build())
            .build();

    private IMQRepository mqRepository;

    public SimpleMQCallback(IMQRepository mqMessageService) {
        this.mqRepository = mqMessageService;
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("correlationData: {} - ack: {}", correlationData.getId(), ack);
        MQMessage[] holder = new MQMessage[1];
        if (isEmpty(holder[0])) {
            holder[0] = mqRepository.getById(correlationData.getId().replaceAll("-", ""));
        }
        Assert.isTrue(isNotEmpty(holder[0]), correlationData.getId() + " 未知的消息");
        if (!ack) {
            log.info("投递交换机失败：{} 原因：{}", correlationData.getId(), cause);
            holder[0].setMessageStatus(MessageStatus.SENT_ERROR.getValue());
            holder[0].setRemark(cause);
            holder[0].setUpdateTime(new Date());
        } else {
            holder[0].setMessageStatus(MessageStatus.SENT.getValue());
            holder[0].setUpdateTime(new Date());
        }
        CompletableFuture.runAsync(() -> {
            mqRepository.updateById(holder[0]);
        }, callbackExecutor);
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("returnedMessage: {}", returned);
        String msgId = returned.getMessage().getMessageProperties().getHeader(RETURNED_MESSAGE_CORRELATION_KEY);
        MQMessage[] holder = new MQMessage[1];
        if (isEmpty(holder[0])) {
            holder[0] = mqRepository.getById(msgId.replaceAll("-", ""));
        }
        Assert.isTrue(isNotEmpty(holder[0]), msgId + " 未知的消息");
        holder[0].setMessageStatus(MessageStatus.SENT_ERROR.getValue());
        holder[0].setRemark( returned.getReplyCode() + " - " + returned.getReplyText());
        holder[0].setUpdateTime(new Date());
        CompletableFuture.runAsync(() -> {
            mqRepository.updateById(holder[0]);
        }, callbackExecutor);
    }
}

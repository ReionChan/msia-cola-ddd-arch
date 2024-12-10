package io.github.reionchan.payment.mq;

import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.BizException;
import com.rabbitmq.client.Channel;
import io.github.reionchan.payment.api.IPaymentService;
import io.github.reionchan.payment.dto.command.PaymentCreateCmd;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.function.Consumer;

import static org.springframework.amqp.support.AmqpHeaders.CORRELATION_ID;

/**
 * @author Reion
 * @date 2024-07-05
 **/
@Slf4j
@Component
public class PaymentMQConsumer {

    @Resource
    private IPaymentService paymentService;

    @Bean
    public Consumer<org.springframework.messaging.Message<String>> stockSubConsumer() {
        return message -> {
            long deliveryTag = message.getHeaders().get(AmqpHeaders.DELIVERY_TAG, Long.class);
            Channel channel = message.getHeaders().get(AmqpHeaders.CHANNEL, Channel.class);
            String msgBodyStr = message.getPayload();
            log.info("接收消息：{}", msgBodyStr);
            String msgId = message.getHeaders().get(CORRELATION_ID, String.class);

            Long orderId = Long.parseLong(msgBodyStr);
            Assert.isTrue(orderId>0, "无效的订单ID");
            log.info("correlationId：{} - orderId: {} - deliveryTag: {}", msgId, orderId, deliveryTag);

            PaymentCreateCmd cmd = PaymentCreateCmd.builder().orderId(orderId).build();
            try {
                SingleResponse<Long> response = paymentService.createPayment(cmd);
                Assert.isTrue(response.getData() > 0, "支付记录创建失败");
                channel.basicAck(deliveryTag, false);
                log.info("订单 {} 生成支付记录 {} 成功", orderId, response.getData());
            } catch (Exception e) {
                try {
                    channel.basicNack(deliveryTag, false, false);
                } catch (IOException ex) {
                    throw new BizException(cmd.getBizScenario().getUniqueIdentity(), "支付记录创建异常:" + ex.getMessage());
                }
                throw new BizException(cmd.getBizScenario().getUniqueIdentity(), "支付记录创建异常:" + e.getMessage());
            }
        };
    }
}

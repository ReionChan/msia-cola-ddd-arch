package io.github.reionchan.payment.mq;

import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.BizException;
import com.rabbitmq.client.Channel;
import io.github.reionchan.payment.api.IPaymentService;
import io.github.reionchan.payment.dto.command.PaymentCreateCmd;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.IOException;

import static io.github.reionchan.mq.consts.RabbitMQConst.HEADER_MESSAGE_CORRELATION;
import static io.github.reionchan.payment.consts.RabbitMQConst.STOCK_SUB_QUEUE;

/**
 * @author Reion
 * @date 2024-07-05
 **/
@Slf4j
@Component
public class PaymentMQConsumer {

    @Resource
    private IPaymentService paymentService;

    @RabbitListener(queues = STOCK_SUB_QUEUE)
    @Transactional(rollbackFor = Exception.class)
    public void consumer(Channel channel, Message message) throws IOException {
        String msgBodyStr = new String(message.getBody());
        log.info("接收消息：{}", msgBodyStr);
        String msgId = message.getMessageProperties().getHeader(HEADER_MESSAGE_CORRELATION);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

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
            channel.basicNack(deliveryTag, false, true);
            throw new BizException(cmd.getBizScenario().getUniqueIdentity(), "支付记录创建异常:" + e.getMessage());
        }
    }
}

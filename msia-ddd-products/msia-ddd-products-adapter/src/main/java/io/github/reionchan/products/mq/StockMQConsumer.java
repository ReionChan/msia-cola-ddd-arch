package io.github.reionchan.products.mq;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.rabbitmq.client.Channel;
import io.github.reionchan.core.model.entity.MQMessage;
import io.github.reionchan.core.mq.MQManager;
import io.github.reionchan.orders.api.IOrderRpc;
import io.github.reionchan.orders.consts.OrderStatus;
import io.github.reionchan.products.api.IStockService;
import io.github.reionchan.products.convertor.MessageConvertor;
import io.github.reionchan.products.dto.StockDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static io.github.reionchan.commons.validation.CommonUtil.isEmpty;
import static io.github.reionchan.mq.consts.RabbitMQConst.HEADER_MESSAGE_CORRELATION;
import static io.github.reionchan.orders.consts.RabbitMQConst.ORDER_PAY_QUEUE;

/**
 * @author Reion
 * @date 2024-07-05
 **/
@Slf4j
@Component
public class StockMQConsumer {
    private static final TypeReference<Map<Long, Set<StockDTO>>> MESSAGE_TYPE = new TypeReference<Map<Long, Set<StockDTO>>>() {
    };

    @Resource
    private IStockService stockService;
    @Resource
    private MessageConvertor messageConvertor;
    @Resource
    private MQManager mqManager;
    @Resource
    private IOrderRpc orderRpc;

    @RabbitListener(queues = ORDER_PAY_QUEUE)
    public void consumer(Channel channel, Message message) throws IOException {
        String msgBodyStr = new String(message.getBody());
        log.info("接收消息：{}", msgBodyStr);
        String msgId = message.getMessageProperties().getHeader(HEADER_MESSAGE_CORRELATION);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            Map<Long, Set<StockDTO>> pair = JSON.parseObject(msgBodyStr, MESSAGE_TYPE);
            Optional<Map.Entry<Long, Set<StockDTO>>> optional = pair.entrySet().stream().findFirst();
            Assert.isTrue(optional.isPresent(), "参数错误");
            Map.Entry<Long, Set<StockDTO>> entry = optional.get();
            log.info("correlationId：{} - orderId: {} - deliveryTag: {}", msgId, entry.getKey(), deliveryTag);

            Set<StockDTO> stockDtoSet = entry.getValue();
            Long orderId = entry.getKey();
            log.info("库存扣减事务执行开始，订单 ID：{}", orderId);

            List<StockDTO> outOfStocks = stockService.trySubStock(orderId, stockDtoSet);

            // 库存不足：回写消息原因，作废订单，确认消息
            if(!isEmpty(outOfStocks)) {
                MQMessage mqMessage = messageConvertor.subStockError2Message(msgId, msgBodyStr, outOfStocks);
                Assert.isTrue(mqManager.Update(mqMessage), "订单库存扣减消息入库异常");
                Response ret = orderRpc.modify(orderId, OrderStatus.OUT_OF_STOCK.getValue());
                Assert.isTrue(ret.isSuccess(), "作废订单异常");
                channel.basicAck(deliveryTag, false);
                log.info("订单：{} 库存不足", orderId);
                return;
            }
            channel.basicAck(deliveryTag, false);
            log.info("订单：{} 库存扣减成功", orderId);

        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, true);
            throw new BizException("库存扣减事务执行失败", e);
        }
    }
}

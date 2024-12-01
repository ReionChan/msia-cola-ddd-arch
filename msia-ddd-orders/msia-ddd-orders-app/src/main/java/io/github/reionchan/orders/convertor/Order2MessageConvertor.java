package io.github.reionchan.orders.convertor;

import cn.hutool.core.util.IdUtil;
import com.alibaba.cola.exception.BizException;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.reionchan.mq.consts.MessageStatus;
import io.github.reionchan.mq.model.entity.MQMessage;
import io.github.reionchan.products.dto.StockDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static io.github.reionchan.orders.consts.RabbitMQConst.ORDER_PAY_EXCHANGE;
import static io.github.reionchan.orders.consts.RabbitMQConst.ORDER_PAY_ROUTING_KEY;

/**
 * @author Reion
 * @date 2024-11-22
 **/
@Component
public class Order2MessageConvertor {

    @Resource
    private ObjectMapper objectMapper;

    public MQMessage orderStock2Message(Map<Long, Set<StockDTO>> pair) {
        try {
            return MQMessage.builder()
                    .messageId(IdUtil.simpleUUID())
                    .content(objectMapper.writeValueAsString(pair))
                    .toExchange(ORDER_PAY_EXCHANGE)
                    .routingKey(ORDER_PAY_ROUTING_KEY)
                    .classType(pair.getClass().getName())
                    .messageStatus(MessageStatus.NEW.getValue())
                    .build();
        } catch(Exception e) {
            throw new BizException("构建消息异常", e);
        }
    }
}

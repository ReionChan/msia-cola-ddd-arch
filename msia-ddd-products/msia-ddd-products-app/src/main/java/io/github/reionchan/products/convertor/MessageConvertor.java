package io.github.reionchan.products.convertor;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.reionchan.mq.consts.MessageStatus;
import io.github.reionchan.mq.model.entity.MQMessage;
import io.github.reionchan.products.dto.StockDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.github.reionchan.orders.consts.RabbitMQConst.ORDER_PAY_EXCHANGE;
import static io.github.reionchan.orders.consts.RabbitMQConst.ORDER_PAY_ROUTING_KEY;
import static io.github.reionchan.products.consts.RabbitMQConst.STOCK_SUB_EXCHANGE;
import static io.github.reionchan.products.consts.RabbitMQConst.STOCK_SUB_ROUTING_KEY;

/**
 * @author Reion
 * @date 2024-11-22
 **/
@Component
public class MessageConvertor {

    @Resource
    private ObjectMapper objectMapper;

    public MQMessage subStockError2Message(String msgId, String msg, List<StockDTO> outOfStocks) {
        try {
            return MQMessage.builder()
                    .messageId(msgId)
                    .content(msg)
                    .toExchange(ORDER_PAY_EXCHANGE)
                    .routingKey(ORDER_PAY_ROUTING_KEY)
                    .classType(Map.class.getName())
                    .messageStatus(MessageStatus.SENT_ERROR.getValue())
                    .remark(objectMapper.writeValueAsString(outOfStocks))
                    .updateTime(new Date())
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public MQMessage subStockSuccess2Message(Long orderId) {
        return MQMessage.builder()
                .messageId(IdUtil.simpleUUID())
                .content(String.valueOf(orderId))
                .toExchange(STOCK_SUB_EXCHANGE)
                .routingKey(STOCK_SUB_ROUTING_KEY)
                .classType(Map.class.getName())
                .messageStatus(MessageStatus.NEW.getValue())
                .createTime(new Date())
                .updateTime(new Date())
                .build();
    }
}

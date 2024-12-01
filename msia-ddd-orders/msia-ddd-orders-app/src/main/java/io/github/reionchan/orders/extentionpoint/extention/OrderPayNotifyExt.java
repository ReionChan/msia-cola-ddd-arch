package io.github.reionchan.orders.extentionpoint.extention;

import com.alibaba.cola.extension.Extension;
import com.alibaba.fastjson.JSON;
import io.github.reionchan.core.mq.MQManager;
import io.github.reionchan.mq.model.entity.MQMessage;
import io.github.reionchan.orders.extentionpoint.OrderPayExtPt;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import static io.github.reionchan.orders.consts.OrdersBizScenarioCst.ORDERS_BIZ_ID;
import static io.github.reionchan.orders.consts.OrdersBizScenarioCst.ORDERS_PAY_USE_CASE;
import static io.github.reionchan.orders.consts.RabbitMQConst.ORDER_PAY_EXCHANGE;
import static io.github.reionchan.orders.consts.RabbitMQConst.ORDER_PAY_ROUTING_KEY;

/**
 * @author Reion
 * @date 2024-11-20
 **/
@Slf4j
@Extension(bizId = ORDERS_BIZ_ID, useCase = ORDERS_PAY_USE_CASE)
public class OrderPayNotifyExt implements OrderPayExtPt {

    @Resource
    private MQManager mqManager;

    @Override
    public void notify(MQMessage message) {
        log.info(JSON.toJSONString(message));
        mqManager.sendMessage(ORDER_PAY_EXCHANGE, ORDER_PAY_ROUTING_KEY, message, new CorrelationData(message.getMessageId()));
    }
}

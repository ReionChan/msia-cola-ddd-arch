package io.github.reionchan.orders.consts;

/**
 * @author Reion
 * @date 2024-07-05
 **/
public interface RabbitMQConst {
    // 订单支付
    String ORDER_PAY_EXCHANGE = "order.pay.exchange";
    String ORDER_PAY_ROUTING_KEY = "order.pay.routing";

    // SpringCloud Stream 模式
    String ORDER_PAY_DESTINATION = "orderPay";
    String ORDER_PAY_GROUP = "orderPayGrp";
    String ORDER_PAY_QUEUE = ORDER_PAY_EXCHANGE + "." + ORDER_PAY_GROUP;
}
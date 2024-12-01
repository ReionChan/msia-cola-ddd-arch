package io.github.reionchan.orders.consts;

/**
 * @author Reion
 * @date 2024-07-05
 **/
public interface RabbitMQConst {
    // 订单支付
    String ORDER_PAY_EXCHANGE = "order.pay.exchange";
    String ORDER_PAY_QUEUE = "order.pay.queue";
    String ORDER_PAY_ROUTING_KEY = "order.pay.routing";
    String ORDER_PAY_DLX = "order.pay.dlx";
    String ORDER_PAY_DLX_ROUTING_KEY = "order.pay.dlx.routing.key";
    String ORDER_PAY_DLQ = ORDER_PAY_QUEUE + ".dlq";
}

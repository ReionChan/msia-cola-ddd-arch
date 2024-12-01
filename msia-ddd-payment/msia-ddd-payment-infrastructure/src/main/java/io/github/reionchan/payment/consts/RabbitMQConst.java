package io.github.reionchan.payment.consts;

/**
 * @author Reion
 * @date 2024-07-05
 **/
public interface RabbitMQConst {
    // 库存扣减
    String STOCK_SUB_EXCHANGE = "stock.sub.exchange";
    String STOCK_SUB_QUEUE = "stock.sub.queue";
    String STOCK_SUB_ROUTING_KEY = "stock.sub.routing";
    String STOCK_SUB_DLX = "stock.sub.dlx";
    String STOCK_SUB_DLX_ROUTING_KEY = "stock.sub.dlx.routing.key";
    String STOCK_SUB_DLQ = STOCK_SUB_QUEUE + ".dlq";
}

package io.github.reionchan.orders.event;

/**
 * @author Reion
 * @date 2024-11-30
 **/
public enum OrderEventType {
    /**
     * 支付事件
     */
    PAY_ORDER,
    /**
     * 修改订单事件
     */
    MODIFY_ORDER,
    /**
     * 库存不足事件
     */
    ORDER_OUT_OF_STOCK,
    /**
     * 支付成功确认事件
     */
    PAY_ORDER_CONFIRMED,
    /**
     * 物流发货事件
     */
    DELIVER_ORDER,
    /**
     * 订单签收事件
     */
    RECEIVED_ORDER,
    /**
     * 回退订单事件
     */
    RETURN_ORDER,
    /**
     * 退款事件
     */
    REFUND_ORDER,
    /**
     * 取消事件
     */
    CANCEL_ORDER,
    /**
     * 删除事件
     */
    DELETE_ORDER,
}

package io.github.reionchan.orders.event.action;

import com.alibaba.cola.statemachine.Action;
import io.github.reionchan.orders.event.OrderEventContext;
import io.github.reionchan.orders.event.OrderEventType;
import io.github.reionchan.orders.model.aggregate.OrderAggregate;
import io.github.reionchan.orders.model.entity.Order;
import io.github.reionchan.orders.model.vo.OrderStatus;
import lombok.extern.slf4j.Slf4j;

import static io.github.reionchan.orders.model.vo.OrderStatus.PAYING;

/**
 * @author Reion
 * @date 2024-11-30
 **/
@Slf4j
public class OrderPayAction implements Action<OrderStatus, OrderEventType, OrderEventContext> {

    @Override
    public void execute(OrderStatus from, OrderStatus to, OrderEventType event, OrderEventContext context) {
        log.debug("orderId {} has been paid!", context.getAggregate().getOrderId());
        OrderAggregate aggregate = context.getAggregate();
        Order order = aggregate.getOrder();
        order.setOrderStatus(PAYING);
    }
}

package io.github.reionchan.orders.event.condition;

import com.alibaba.cola.statemachine.Condition;
import io.github.reionchan.orders.event.OrderEventContext;

/**
 * @author Reion
 * @date 2024-11-29
 **/
public class OrderDeliverCondition implements Condition<OrderEventContext> {
    @Override
    public boolean isSatisfied(OrderEventContext context) {
        return true;
    }
}

package io.github.reionchan.orders.event;

import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import io.github.reionchan.orders.event.action.*;
import io.github.reionchan.orders.event.condition.*;
import io.github.reionchan.orders.model.vo.OrderStatus;

import static io.github.reionchan.orders.event.OrderEventType.*;
import static io.github.reionchan.orders.model.vo.OrderStatus.*;

/**
 * @author Reion
 * @date 2024-11-29
 **/
public class OrderStateMachine {
    public static final String ORDER_STATE_MACHINE_ID = "orderStateMachine";

    public OrderStateMachine() {
        this.init();
    }

    private void init() {
        StateMachineBuilder<OrderStatus, OrderEventType, OrderEventContext> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(UNPAID)
                .to(PAYING)
                .on(PAY_ORDER)
                .when(new OrderPayCondition())
                .perform(new OrderPayAction());

        builder.internalTransition()
                .within(UNPAID)
                .on(MODIFY_ORDER)
                .perform(new OrderModifyAction());

        builder.externalTransition()
                .from(PAYING)
                .to(PAID)
                .on(PAY_ORDER_CONFIRMED)
                .when(new OrderPayConfirmCondition())
                .perform(new OrderPayConfirmAction());

        builder.externalTransition()
                .from(PAYING)
                .to(OUT_OF_STOCK)
                .on(ORDER_OUT_OF_STOCK)
                .when(new OrderOutOfStockCondition())
                .perform(new OrderOutOfStockAction());

        builder.externalTransition()
                .from(PAID)
                .to(DELIVERED)
                .on(DELIVER_ORDER)
                .when(new OrderDeliverCondition())
                .perform(new OrderDeliverAction());

        builder.externalTransition()
                .from(DELIVERED)
                .to(RECEIVED)
                .on(RECEIVED_ORDER)
                .when(new OrderReceiveCondition())
                .perform(new OrderReceiveAction());

        builder.externalTransitions()
                .fromAmong(DELIVERED, RECEIVED)
                .to(RETURNED)
                .on(RETURN_ORDER)
                .when(new OrderReturnCondition())
                .perform(new OrderReturnAction());

        builder.externalTransition()
                .from(RETURNED)
                .to(RETURNED_REFUND)
                .on(REFUND_ORDER)
                .when(new OrderRefundCondition())
                .perform(new OrderRefundAction());

        builder.externalTransitions()
                .fromAmong(UNPAID, OUT_OF_STOCK)
                .to(CANCEL)
                .on(CANCEL_ORDER)
                .when(new OrderCancelCondition())
                .perform(new OrderCancelAction());

        builder.externalTransitions()
                .fromAmong(UNPAID, CANCEL, OUT_OF_STOCK)
                .to(DELETED)
                .on(DELETE_ORDER)
                .when(new OrderDeleteCondition())
                .perform(new OrderDeleteAction());

        builder.setFailCallback((s, e, c) -> {
            Assert.isTrue(false, s.getName() + " 不满足 " + e.name() + " 前置要求！");
        });

        builder.build(ORDER_STATE_MACHINE_ID);
    }
}

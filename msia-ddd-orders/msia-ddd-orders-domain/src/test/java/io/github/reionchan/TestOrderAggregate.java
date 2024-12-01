package io.github.reionchan;

import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.StateMachineFactory;
import io.github.reionchan.orders.event.OrderEventContext;
import io.github.reionchan.orders.event.OrderEventType;
import io.github.reionchan.orders.event.OrderStateMachine;
import io.github.reionchan.orders.gateway.IOrderAggregateGateway;
import io.github.reionchan.orders.model.aggregate.OrderAggregate;
import io.github.reionchan.orders.model.entity.Order;
import io.github.reionchan.orders.model.vo.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.reionchan.orders.event.OrderStateMachine.ORDER_STATE_MACHINE_ID;
import static io.github.reionchan.orders.model.vo.OrderStatus.PAID;
import static io.github.reionchan.orders.model.vo.OrderStatus.PAYING;
import static org.mockito.Mockito.when;

/**
 * @author Reion
 * @date 2024-12-01
 **/
@ExtendWith(MockitoExtension.class)
public class TestOrderAggregate {
    private static OrderStateMachine orderStateMachine;
    private StateMachine<OrderStatus, OrderEventType, OrderEventContext> machine;
    @Mock
    private IOrderAggregateGateway orderAggregateGateway;
    @Spy
    private OrderAggregate orderAggregate;

    @BeforeAll
    static void setUpAll() {
        orderStateMachine = new OrderStateMachine();
    }

    @BeforeEach
    void setUp() {
        machine = StateMachineFactory.get(ORDER_STATE_MACHINE_ID);
        orderAggregate.setOrderStateMachine(machine);
        orderAggregate.setOrderAggregateGateway(orderAggregateGateway);
    }

    @Test
    public void testUpdateStatus() {
        // setup
        Order order = new Order();
        order.setOrderStatus(PAYING);
        OrderEventContext ctx = new OrderEventContext();
        ctx.setAggregate(orderAggregate);
        orderAggregate.setOrder(order);
        // config
        when(orderAggregate.getOrder()).thenReturn(order);
        when(orderAggregateGateway.updateStatus(orderAggregate)).thenReturn(true);
        // run the test
        boolean flag = orderAggregate.updateStatus(PAID.getValue());
        // expected
        Assertions.assertTrue(flag);
        Assertions.assertEquals(PAID, order.getOrderStatus());
    }
}

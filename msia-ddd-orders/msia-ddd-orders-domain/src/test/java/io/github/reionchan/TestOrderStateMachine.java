package io.github.reionchan;

import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.StateMachineFactory;
import io.github.reionchan.orders.event.OrderEventContext;
import io.github.reionchan.orders.event.OrderEventType;
import io.github.reionchan.orders.event.OrderStateMachine;
import io.github.reionchan.orders.model.aggregate.OrderAggregate;
import io.github.reionchan.orders.model.entity.Order;
import io.github.reionchan.orders.model.vo.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.github.reionchan.orders.event.OrderEventType.*;
import static io.github.reionchan.orders.event.OrderStateMachine.ORDER_STATE_MACHINE_ID;
import static io.github.reionchan.orders.model.vo.OrderStatus.*;
import static org.mockito.Mockito.when;

/**
 * @author Reion
 * @date 2024-11-30
 **/
@ExtendWith(MockitoExtension.class)
public class TestOrderStateMachine {

    private static OrderStateMachine orderStateMachine;
    private static StateMachine<OrderStatus, OrderEventType, OrderEventContext> machine;
    @Mock
    private OrderAggregate orderAggregate;

    @BeforeAll
    static void setUpAll() {
        try{
            StateMachineFactory.get(ORDER_STATE_MACHINE_ID);
        } catch (Exception e) {
            orderStateMachine = new OrderStateMachine();
        }
        machine = StateMachineFactory.get(ORDER_STATE_MACHINE_ID);
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    public void testUML() {
        System.out.println(machine.generatePlantUML());
    }

    @Test
    public void testUnpaid2Paying() {
        // setup
        Order order = new Order();
        order.setOrderStatus(UNPAID);
        OrderEventContext ctx = new OrderEventContext();
        ctx.setAggregate(orderAggregate);
        // config
        when(orderAggregate.getOrder()).thenReturn(order);
        // run the test
        boolean flag = machine.verify(orderAggregate.getOrder().getOrderStatus(), PAY_ORDER);
        OrderStatus status = machine.fireEvent(orderAggregate.getOrder().getOrderStatus(), PAY_ORDER, ctx);
        // expected
        Assertions.assertTrue(flag);
        Assertions.assertEquals(PAYING, status);
        Assertions.assertEquals(status, order.getOrderStatus());
    }

    @Test
    public void testUnpaid2Canceled() {
        // setup
        Order order = new Order();
        order.setOrderStatus(UNPAID);
        OrderEventContext ctx = new OrderEventContext();
        ctx.setAggregate(orderAggregate);
        // config
        when(orderAggregate.getOrder()).thenReturn(order);
        // run the test
        boolean flag = machine.verify(orderAggregate.getOrder().getOrderStatus(), CANCEL_ORDER);
        OrderStatus status = machine.fireEvent(orderAggregate.getOrder().getOrderStatus(), CANCEL_ORDER, ctx);
        // expected
        Assertions.assertTrue(flag);
        Assertions.assertEquals(CANCEL, status);
        Assertions.assertEquals(status, order.getOrderStatus());
    }

    @Test
    public void testUnpaidModify() {
        // setup
        Order order = new Order();
        order.setOrderStatus(UNPAID);
        OrderEventContext ctx = new OrderEventContext();
        ctx.setAggregate(orderAggregate);
        // config
        when(orderAggregate.getOrder()).thenReturn(order);
        // run the test
        boolean flag = machine.verify(orderAggregate.getOrder().getOrderStatus(), MODIFY_ORDER);
        OrderStatus status = machine.fireEvent(orderAggregate.getOrder().getOrderStatus(), MODIFY_ORDER, ctx);
        // expected
        Assertions.assertTrue(flag);
        Assertions.assertEquals(UNPAID, status);
        Assertions.assertEquals(status, order.getOrderStatus());
    }

    @Test
    public void testPaying2Paid() {
        // setup
        Order order = new Order();
        order.setOrderStatus(PAYING);
        OrderEventContext ctx = new OrderEventContext();
        ctx.setAggregate(orderAggregate);
        // config
        when(orderAggregate.getOrder()).thenReturn(order);
        // run the test
        boolean flag = machine.verify(orderAggregate.getOrder().getOrderStatus(), PAY_ORDER_CONFIRMED);
        OrderStatus status = machine.fireEvent(orderAggregate.getOrder().getOrderStatus(), PAY_ORDER_CONFIRMED, ctx);
        // expected
        Assertions.assertTrue(flag);
        Assertions.assertEquals(PAID, status);
        Assertions.assertEquals(status, order.getOrderStatus());
    }

    @Test
    public void testPaid2Delivered() {
        // setup
        Order order = new Order();
        order.setOrderStatus(PAID);
        OrderEventContext ctx = new OrderEventContext();
        ctx.setAggregate(orderAggregate);
        // config
        when(orderAggregate.getOrder()).thenReturn(order);
        // run the test
        boolean flag = machine.verify(orderAggregate.getOrder().getOrderStatus(), DELIVER_ORDER);
        OrderStatus status = machine.fireEvent(orderAggregate.getOrder().getOrderStatus(), DELIVER_ORDER, ctx);
        // expected
        Assertions.assertTrue(flag);
        Assertions.assertEquals(DELIVERED, status);
        Assertions.assertEquals(status, order.getOrderStatus());
    }
}

package io.github.reionchan.orders.model.aggregate;

import com.alibaba.cola.domain.Entity;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.StateMachineFactory;
import io.github.reionchan.orders.event.OrderEvent;
import io.github.reionchan.orders.event.OrderEventContext;
import io.github.reionchan.orders.event.OrderEventType;
import io.github.reionchan.orders.gateway.IOrderAggregateGateway;
import io.github.reionchan.orders.model.entity.Order;
import io.github.reionchan.orders.model.entity.OrderDetail;
import io.github.reionchan.orders.model.vo.OrderStatus;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import static io.github.reionchan.orders.event.OrderEventType.*;
import static io.github.reionchan.orders.event.OrderStateMachine.ORDER_STATE_MACHINE_ID;


/**
 * @author Reion
 * @date 2024-11-24
 **/
@Slf4j
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "orderId")
public class OrderAggregate implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long orderId;

    private Order order;

    private Set<OrderDetail> orderDetailSet;

    @Resource
    private IOrderAggregateGateway orderAggregateGateway;

    private StateMachine<OrderStatus, OrderEventType, OrderEventContext> orderStateMachine =
            StateMachineFactory.get(ORDER_STATE_MACHINE_ID);

    /**
     * 计算订单、订单明细价格
     */
    public BigDecimal calcOrderTotalPrice() {
        Assert.notNull(order, "订单为空");
        Assert.isTrue(Objects.nonNull(orderDetailSet) && orderDetailSet.size()>0, "订单明细为空");
        int totalAmount = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;
        for(OrderDetail detail : orderDetailSet) {
            detail.setSumPrice(detail.getPrice().multiply(BigDecimal.valueOf(detail.getAmount())));
            totalAmount += detail.getAmount();
            totalPrice = totalPrice.add(detail.getSumPrice());
        }
        order.setTotalAmount(totalAmount);
        order.setTotalPrice(totalPrice);
        return order.getTotalPrice();
    }

    /**
     * 新建订单
     */
    public boolean save() {
        calcOrderTotalPrice();
        return orderAggregateGateway.save(this);
    }

    /**
     * 修改订单
     */
    public boolean update(Set<OrderDetail> needRemoveSet) {
        calcOrderTotalPrice();
        OrderEventContext ctx = new OrderEventContext();
        ctx.setAggregate(this);
        OrderEvent modifyOrderEvent = new OrderEvent(MODIFY_ORDER, ctx);
        fireEvent(modifyOrderEvent);
        boolean ret = true;
        if(needRemoveSet.size()>0) {
            ret = orderAggregateGateway.removeDetail(needRemoveSet);
        }
        return ret && orderAggregateGateway.updateWithDetails(this);
    }

    /**
     * 删除订单
     */
    public boolean delete() {
        OrderEventContext ctx = new OrderEventContext();
        ctx.setAggregate(this);
        OrderEvent deleteOrderEvent = new OrderEvent(DELETE_ORDER, ctx);
        fireEvent(deleteOrderEvent);
        return orderAggregateGateway.delete(this);
    }

    /**
     * 支付订单
     */
    public boolean payOrder() {
        OrderEventContext ctx = new OrderEventContext();
        ctx.setAggregate(this);
        OrderEvent payOrderEvent = new OrderEvent(PAY_ORDER, ctx);
        fireEvent(payOrderEvent);
        return orderAggregateGateway.updateStatus(this);
    }

    /**
     * 更新支付状态
     */
    public boolean updateStatus(Byte status) {
        OrderStatus toStatus = OrderStatus.of(status);
        OrderEventContext ctx = new OrderEventContext();
        ctx.setAggregate(this);
        OrderEvent event;
        switch (toStatus) {
            case PAID:
                event = new OrderEvent(PAY_ORDER_CONFIRMED, ctx);
                break;
            case OUT_OF_STOCK:
                event = new OrderEvent(ORDER_OUT_OF_STOCK, ctx);
                break;
            default:
                event = null;
        }
        Assert.notNull(event, "暂不支持更新为 " + toStatus.getName() + " 的操作");
        fireEvent(event);
        return orderAggregateGateway.updateStatus(this);
    }

    /**
     * 状态机触发事件
     */
    private OrderStatus fireEvent(OrderEvent event) {
        return orderStateMachine.fireEvent(this.order.getOrderStatus(), event.getEventType(), (OrderEventContext) event.getSource());
    }
}

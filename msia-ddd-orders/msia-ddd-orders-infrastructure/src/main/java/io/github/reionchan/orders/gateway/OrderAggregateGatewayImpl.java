package io.github.reionchan.orders.gateway;

import com.alibaba.cola.domain.DomainFactory;
import com.alibaba.cola.exception.Assert;
import io.github.reionchan.orders.convertor.OrderDetailEntityConvertor;
import io.github.reionchan.orders.database.dataobject.OrderDetailDO;
import io.github.reionchan.orders.model.aggregate.OrderAggregate;
import io.github.reionchan.orders.model.entity.Order;
import io.github.reionchan.orders.model.entity.OrderDetail;
import io.github.reionchan.orders.repository.IOrderDetailRepository;
import io.github.reionchan.orders.repository.IOrderRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Component
public class OrderAggregateGatewayImpl implements IOrderAggregateGateway {
    @Resource
    private IOrderRepository orderRepository;
    @Resource
    private IOrderDetailRepository orderDetailRepository;
    @Resource
    private OrderDetailEntityConvertor detailEntityConvertor;

    @Override
    public OrderAggregate getOrderAggById(Long orderId) {
        Order order = orderRepository.getOrderById(orderId);
        Assert.notNull(order, "订单不存在");
        Set<OrderDetail> orderDetailSet = orderDetailRepository.getOrderDetailList(orderId);
        OrderAggregate orderAggregate = DomainFactory.create(OrderAggregate.class);
        orderAggregate.setOrderId(order.getId());
        orderAggregate.setOrder(order);
        orderAggregate.setOrderDetailSet(orderDetailSet);
        return orderAggregate;
    }

    @Override
    public boolean save(OrderAggregate orderAggregate) {
        Assert.isTrue(orderRepository.save(orderAggregate.getOrder()), "保存订单失败");
        Long orderId = orderAggregate.getOrder().getId();
        orderAggregate.setOrderId(orderId);
        orderAggregate.getOrderDetailSet().forEach(detail -> {
            detail.setOrderId(orderId);
        });
        Assert.isTrue(orderDetailRepository.saveBatch(detailEntityConvertor.toDataObjectSet(orderAggregate.getOrderDetailSet())), "保存订单明细失败");
        return true;
    }

    @Override
    public boolean updateWithDetails(OrderAggregate orderAggregate) {
        Assert.isTrue(orderRepository.update(orderAggregate.getOrder()), "保存订单失败");
        Assert.isTrue(orderDetailRepository.updateBatchById(detailEntityConvertor.toDataObjectSet(orderAggregate.getOrderDetailSet())), "更新订单明细失败");
        return true;
    }

    @Override
    public boolean removeDetail(Set<OrderDetail> needRemoveSet) {
        Assert.isTrue(orderDetailRepository.removeBatchByIds(detailEntityConvertor.toDataObjectSet(needRemoveSet)), "移除订单明细失败");
        return true;
    }

    @Override
    public boolean updateStatus(OrderAggregate orderAggregate) {
        Assert.isTrue(orderRepository.update(orderAggregate.getOrder()), "修改订单状态失败");
        return true;
    }

    @Override
    public boolean delete(OrderAggregate orderAggregate) {
        Assert.isTrue(orderRepository.update(orderAggregate.getOrder()), "删除订单失败");
        return true;
    }
}

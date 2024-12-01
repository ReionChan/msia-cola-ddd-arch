package io.github.reionchan.orders.gateway;

import io.github.reionchan.orders.model.aggregate.OrderAggregate;
import io.github.reionchan.orders.model.entity.OrderDetail;

import java.util.Set;

/**
 * @author Reion
 * @date 2024-11-19
 **/
public interface IOrderAggregateGateway {
    OrderAggregate getOrderAggById(Long orderId);

    boolean save(OrderAggregate orderAggregate);

    boolean updateWithDetails(OrderAggregate orderAggregate);

    boolean delete(OrderAggregate orderAggregate);

    boolean updateStatus(OrderAggregate orderAggregate);

    boolean removeDetail(Set<OrderDetail> needRemoveSet);
}

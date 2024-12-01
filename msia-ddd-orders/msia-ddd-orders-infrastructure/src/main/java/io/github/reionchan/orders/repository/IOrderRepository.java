package io.github.reionchan.orders.repository;

import io.github.reionchan.orders.model.entity.Order;

/**
 * @author Reion
 * @date 2024-11-21
 **/
public interface IOrderRepository {

    Order getOrderById(Long orderId);

    boolean save(Order order);

    boolean update(Order order);
}

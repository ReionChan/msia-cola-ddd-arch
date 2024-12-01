package io.github.reionchan.orders.event;

import io.github.reionchan.orders.model.aggregate.OrderAggregate;
import lombok.Data;

/**
 * @author Reion
 * @date 2024-11-29
 **/
@Data
public class OrderEventContext {
    private OrderAggregate aggregate;
}

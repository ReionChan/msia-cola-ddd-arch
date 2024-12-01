package io.github.reionchan.orders.event;

import lombok.Getter;

import java.util.EventObject;

/**
 * @author Reion
 * @date 2024-11-29
 **/
@Getter
public class OrderEvent extends EventObject {

    private OrderEventType eventType;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public OrderEvent(OrderEventType eventType, OrderEventContext source) {
        super(source);
        this.eventType = eventType;
    }
}

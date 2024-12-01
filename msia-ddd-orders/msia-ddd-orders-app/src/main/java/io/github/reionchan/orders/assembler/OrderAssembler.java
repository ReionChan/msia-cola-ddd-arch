package io.github.reionchan.orders.assembler;

import io.github.reionchan.commons.bean.DTOAssembler;
import io.github.reionchan.orders.dto.OrderDTO;
import io.github.reionchan.orders.dto.clientobject.OrderWebCO;
import io.github.reionchan.orders.dto.command.OrderCreationCmd;
import io.github.reionchan.orders.model.entity.Order;
import io.github.reionchan.orders.model.vo.OrderStatus;
import org.mapstruct.*;


/**
 * @author Reion
 * @date 2024-11-16
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderAssembler extends DTOAssembler<OrderDTO, Order> {

    Order toEntity(OrderCreationCmd cmd);

    @AfterMapping
    default void afterToEntity(OrderCreationCmd source, @MappingTarget Order order) {
        order.setOrderStatus(OrderStatus.UNPAID);
    }

    OrderWebCO toClientObject(Order order);
}

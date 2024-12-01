package io.github.reionchan.orders.convertor;

import io.github.reionchan.commons.bean.EntityConvertor;
import io.github.reionchan.orders.database.dataobject.OrderDO;
import io.github.reionchan.orders.model.entity.Order;
import org.mapstruct.*;

import java.util.Date;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderEntityConvertor extends EntityConvertor<Order, OrderDO> {

    @AfterMapping
    default void afterToDataObject(Order source, @MappingTarget OrderDO order) {
        if (isNotEmpty(source.getId()) && source.getId() > 0) {
            order.setUpdateTime(new Date());
        } else {
            order.setCreateTime(new Date());
            order.setUpdateTime(order.getCreateTime());
        }
    }

    @AfterMapping
    default void afterToEntity(OrderDO source, @MappingTarget Order order) {
        order.getOrderStatus(source.getStatus());
    }
}

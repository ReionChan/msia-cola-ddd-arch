package io.github.reionchan.orders.assembler;

import io.github.reionchan.commons.bean.DTOAssembler;
import io.github.reionchan.orders.dto.OrderDetailDTO;
import io.github.reionchan.orders.dto.clientobject.OrderDetailWebCO;
import io.github.reionchan.orders.dto.command.OrderDetailCreationCmd;
import io.github.reionchan.orders.model.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Collection;
import java.util.Set;


/**
 * @author Reion
 * @date 2024-11-16
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderDetailAssembler extends DTOAssembler<OrderDetailDTO, OrderDetail> {

    OrderDetail toEntity(OrderDetailCreationCmd cmd);
    Set<OrderDetail> toEntitySet(Set<OrderDetailCreationCmd> orderDetails);

    OrderDetailWebCO toClientObject(OrderDetail detail);
    Set<OrderDetailWebCO> toClientObjectSet(Collection<OrderDetail> details);
}

package io.github.reionchan.orders.convertor;

import io.github.reionchan.commons.bean.EntityConvertor;
import io.github.reionchan.orders.database.dataobject.OrderDO;
import io.github.reionchan.orders.database.dataobject.OrderDetailDO;
import io.github.reionchan.orders.model.entity.Order;
import io.github.reionchan.orders.model.entity.OrderDetail;
import org.mapstruct.*;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderDetailEntityConvertor extends EntityConvertor<OrderDetail, OrderDetailDO> {

    @AfterMapping
    default void afterToDataObject(OrderDetail source, @MappingTarget OrderDetailDO detailDO) {
        if (isNotEmpty(source.getId()) && source.getId() > 0) {
            detailDO.setUpdateTime(new Date());
        } else {
            detailDO.setCreateTime(new Date());
            detailDO.setUpdateTime(detailDO.getCreateTime());
        }
    }

    Set<OrderDetailDO> toDataObjectSet(Collection<OrderDetail> details);

    Set<OrderDetail> toDataEntitySet(Collection<OrderDetailDO> details);
}

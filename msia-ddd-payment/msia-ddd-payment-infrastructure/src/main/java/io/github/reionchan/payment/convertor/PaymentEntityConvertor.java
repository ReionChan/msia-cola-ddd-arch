package io.github.reionchan.payment.convertor;

import io.github.reionchan.commons.bean.EntityConvertor;
import io.github.reionchan.payment.database.dataobject.PaymentDO;
import io.github.reionchan.payment.model.entity.Payment;
import org.mapstruct.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentEntityConvertor extends EntityConvertor<Payment, PaymentDO> {

    @AfterMapping
    default void afterToEntity(PaymentDO source, @MappingTarget Payment payment) {
        payment.getPlatform(source.getPlatform());
        payment.getStatus(source.getStatus());
    }

    @Override
    default void updateDOFromEntity(Payment payment, @MappingTarget PaymentDO dataObject) {
        if(isNotEmpty(payment.getId())) {
            dataObject.setUpdateTime(new Date());
        } else {
            dataObject.setCreateTime(new Date());
            dataObject.setUpdateTime(dataObject.getCreateTime());
        }
    }

    List<Payment> toEntityList(Collection<PaymentDO> payments);
}

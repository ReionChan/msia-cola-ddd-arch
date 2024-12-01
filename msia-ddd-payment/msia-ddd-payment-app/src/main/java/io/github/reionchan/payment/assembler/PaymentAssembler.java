package io.github.reionchan.payment.assembler;

import io.github.reionchan.commons.bean.DTOAssembler;
import io.github.reionchan.payment.dto.PaymentDTO;
import io.github.reionchan.payment.dto.command.PaymentCreateCmd;
import io.github.reionchan.payment.model.entity.Payment;
import org.mapstruct.*;

import java.util.Collection;
import java.util.List;


/**
 * @author Reion
 * @date 2024-11-16
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentAssembler extends DTOAssembler<PaymentDTO, Payment> {

    Payment toEntity(PaymentCreateCmd cmd);

    @AfterMapping
    default void afterToEntity(PaymentCreateCmd source, @MappingTarget Payment payment) {

    }

    @AfterMapping
    default void afterToEntity(PaymentDTO source, @MappingTarget Payment payment) {
        payment.getStatus(source.getStatus());
        payment.getPlatform(source.getPlatform());
    }

    List<PaymentDTO> toDTOList(Collection<Payment> payments);
}

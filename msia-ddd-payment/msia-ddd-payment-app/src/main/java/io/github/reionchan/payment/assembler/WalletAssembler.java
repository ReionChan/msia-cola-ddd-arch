package io.github.reionchan.payment.assembler;

import io.github.reionchan.commons.bean.DTOAssembler;
import io.github.reionchan.payment.dto.WalletDTO;
import io.github.reionchan.payment.dto.command.WalletCreateCmd;
import io.github.reionchan.payment.model.entity.Wallet;
import org.mapstruct.*;

import java.math.BigDecimal;


/**
 * @author Reion
 * @date 2024-11-16
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WalletAssembler extends DTOAssembler<WalletDTO, Wallet> {

    @AfterMapping
    default void afterToEntity(WalletDTO source, @MappingTarget Wallet wallet) {

    }

    Wallet toEntity(WalletCreateCmd cmd);

    @AfterMapping
    default void afterToEntity(WalletCreateCmd source, @MappingTarget Wallet wallet) {
        wallet.setBlockedBalance(BigDecimal.ZERO);
        wallet.setAvailableBalance(BigDecimal.ZERO);
    }

}

package io.github.reionchan.payment.repository;

import io.github.reionchan.payment.model.entity.Wallet;

import java.math.BigDecimal;

/**
 * @author Reion
 * @date 2024-11-21
 **/
public interface IWalletRepository {

    boolean save(Wallet wallet);

    Wallet existByWalletId(Long id);

    boolean recharge(Long id, BigDecimal rechargeAmount);

    Wallet getWalletForUpdate(Long userId);

    boolean updateById(Wallet wallet);
}

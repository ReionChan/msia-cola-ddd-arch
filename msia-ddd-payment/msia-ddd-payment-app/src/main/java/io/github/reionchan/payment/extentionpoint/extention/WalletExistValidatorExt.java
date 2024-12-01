package io.github.reionchan.payment.extentionpoint.extention;

import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.Extension;
import io.github.reionchan.payment.dto.command.WalletCreateCmd;
import io.github.reionchan.payment.dto.command.WalletRechargeCmd;
import io.github.reionchan.payment.extentionpoint.WalletCreateExtPt;
import io.github.reionchan.payment.extentionpoint.WalletRechargeExtPt;
import io.github.reionchan.payment.model.entity.Wallet;
import io.github.reionchan.payment.repository.impl.WalletRepositoryImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;
import static io.github.reionchan.payment.consts.PaymentBizScenarioCst.*;

/**
 * @author Reion
 * @date 2024-11-20
 **/
@Slf4j
@Extension(bizId = PAYMENT_BIZ_ID, useCase = PAYMENT_WALLET_USE_CASE)
public class WalletExistValidatorExt implements WalletCreateExtPt, WalletRechargeExtPt {

    @Resource
    private WalletRepositoryImpl walletRepository;

    @Override
    public Long validate(WalletCreateCmd cmd) {
        return walletRepository.existByUserId(cmd.getUserId());
    }

    @Override
    public Wallet validate(WalletRechargeCmd cmd) {
        Wallet wallet = walletRepository.existByWalletId(cmd.getId());
        Assert.isTrue(isNotEmpty(wallet), "钱包不存在");
        return wallet;
    }
}

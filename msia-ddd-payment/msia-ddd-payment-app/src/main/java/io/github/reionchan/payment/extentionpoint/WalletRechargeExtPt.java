package io.github.reionchan.payment.extentionpoint;

import com.alibaba.cola.extension.ExtensionPointI;
import io.github.reionchan.payment.dto.command.WalletRechargeCmd;
import io.github.reionchan.payment.model.entity.Wallet;

/**
 * 钱包充值前扩展点
 *
 * @author Reion
 * @date 2024-11-20
 **/
public interface WalletRechargeExtPt extends ExtensionPointI {
    /**
     * 根据钱包ID检查用户钱包是否存在
     * @param cmd
     * @return 钱包
     */
    Wallet validate(WalletRechargeCmd cmd);
}

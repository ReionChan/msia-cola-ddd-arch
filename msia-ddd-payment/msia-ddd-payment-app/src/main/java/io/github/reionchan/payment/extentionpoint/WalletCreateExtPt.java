package io.github.reionchan.payment.extentionpoint;

import com.alibaba.cola.extension.ExtensionPointI;
import io.github.reionchan.payment.dto.command.WalletCreateCmd;

/**
 * 钱包创建前扩展点
 *
 * @author Reion
 * @date 2024-11-20
 **/
public interface WalletCreateExtPt extends ExtensionPointI {
    /**
     * 根据用户ID检查用户钱包是否存在
     * @param cmd
     * @return 钱包ID
     */
    Long validate(WalletCreateCmd cmd);
}

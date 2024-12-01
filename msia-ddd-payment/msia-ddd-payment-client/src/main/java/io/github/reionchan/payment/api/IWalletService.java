package io.github.reionchan.payment.api;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.payment.dto.WalletDTO;
import io.github.reionchan.payment.dto.command.WalletCreateCmd;
import io.github.reionchan.payment.dto.command.WalletRechargeCmd;

/**
 * 用户钱包服务接口
 *
 * @author Reion
 * @date 2023-12-14
 **/
public interface IWalletService {
    SingleResponse<Long> createWallet(WalletCreateCmd cmd);

    Response updateWallet(WalletRechargeCmd walletRechargeCmd);

    WalletDTO getWalletForUpdate(Long userId);
}

package io.github.reionchan.payment.api;

import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.payment.dto.command.WalletCreateCmd;

/**
 * 钱包服务 RPC 接口
 *
 * @author Reion
 * @date 2023-12-20
 **/
public interface IWalletRpc {
    SingleResponse<Long> create(WalletCreateCmd walletCreateCmd);
}

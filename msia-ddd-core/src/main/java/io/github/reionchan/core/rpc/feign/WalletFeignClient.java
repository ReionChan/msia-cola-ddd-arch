package io.github.reionchan.core.rpc.feign;

import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.core.config.FeignClientConfiguration;
import io.github.reionchan.payment.api.IWalletRpc;
import io.github.reionchan.payment.dto.command.WalletCreateCmd;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 钱包 OpenFeign 客户端接口
 *
 * @author Reion
 * @date 2023-12-19
 **/
@FeignClient(name = "msia-payment",
        contextId = "msia-payment.WalletFeignClient",
        configuration = FeignClientConfiguration.class)
public interface WalletFeignClient extends IWalletRpc {
    @Override
    @PostMapping("/rpc/wallet")
    SingleResponse<Long> create(@RequestBody WalletCreateCmd walletCreateCmd);
}

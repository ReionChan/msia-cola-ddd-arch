package io.github.reionchan.payment.gateway;

import io.github.reionchan.payment.repository.IWalletRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Component
public class WalletGatewayImpl implements IWalletGateway {
    @Resource
    private IWalletRepository walletRepository;
}

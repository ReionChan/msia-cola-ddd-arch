package io.github.reionchan.payment.command.query;

import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.payment.assembler.WalletAssembler;
import io.github.reionchan.payment.dto.WalletDTO;
import io.github.reionchan.payment.model.entity.Wallet;
import io.github.reionchan.payment.repository.IWalletRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class WalletQryExe {

    @Resource
    private IWalletRepository walletRepository;
    @Resource
    private WalletAssembler walletAssembler;
    @Resource
    private ExtensionExecutor extensionExecutor;

    public WalletDTO execute(Long userId) {
        // 业务
        Wallet wallet = walletRepository.getWalletForUpdate(userId);
        return isNotEmpty(wallet) ? walletAssembler.toDTO(wallet) : null;
    }
}

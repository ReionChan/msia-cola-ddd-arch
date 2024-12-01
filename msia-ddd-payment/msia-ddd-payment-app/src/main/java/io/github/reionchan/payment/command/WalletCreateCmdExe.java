package io.github.reionchan.payment.command;

import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.payment.assembler.WalletAssembler;
import io.github.reionchan.payment.dto.command.WalletCreateCmd;
import io.github.reionchan.payment.extentionpoint.WalletCreateExtPt;
import io.github.reionchan.payment.model.entity.Wallet;
import io.github.reionchan.payment.repository.IWalletRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class WalletCreateCmdExe {

    @Resource
    private IWalletRepository walletRepository;
    @Resource
    private WalletAssembler walletAssembler;
    @Resource
    private ExtensionExecutor extensionExecutor;

    public SingleResponse<Long> execute(WalletCreateCmd cmd) {
        // 校验
        Long walletId = extensionExecutor.execute(WalletCreateExtPt.class, cmd.getBizScenario(), chk -> chk.validate(cmd));
        if (isNotEmpty(walletId) && walletId > 0) {
            return SingleResponse.of(walletId);
        }
        // 转换
        Wallet wallet = walletAssembler.toEntity(cmd);
        // 业务
        Assert.isTrue(walletRepository.save(wallet), "创建钱包失败");
        return SingleResponse.of(wallet.getId());
    }
}

package io.github.reionchan.payment.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.payment.assembler.WalletAssembler;
import io.github.reionchan.payment.dto.command.WalletRechargeCmd;
import io.github.reionchan.payment.extentionpoint.WalletRechargeExtPt;
import io.github.reionchan.payment.model.entity.Wallet;
import io.github.reionchan.payment.repository.IWalletRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class WalletRechargeCmdExe {

    @Resource
    private IWalletRepository walletRepository;
    @Resource
    private WalletAssembler walletAssembler;
    @Resource
    private ExtensionExecutor extensionExecutor;

    public Response execute(WalletRechargeCmd cmd) {
        // 校验
        Wallet wallet = extensionExecutor.execute(WalletRechargeExtPt.class, cmd.getBizScenario(), chk -> chk.validate(cmd));
        // 业务
        Assert.isTrue(walletRepository.recharge(wallet.getId(), cmd.getRechargeAmount()), "充值钱包失败");
        return Response.buildSuccess();
    }
}

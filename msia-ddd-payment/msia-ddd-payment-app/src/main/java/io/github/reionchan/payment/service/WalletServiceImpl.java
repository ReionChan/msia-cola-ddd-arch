package io.github.reionchan.payment.service;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.payment.api.IWalletService;
import io.github.reionchan.payment.command.WalletCreateCmdExe;
import io.github.reionchan.payment.command.WalletRechargeCmdExe;
import io.github.reionchan.payment.command.query.WalletQryExe;
import io.github.reionchan.payment.dto.WalletDTO;
import io.github.reionchan.payment.dto.command.WalletCreateCmd;
import io.github.reionchan.payment.dto.command.WalletRechargeCmd;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户钱包服务实现
 *
 * @author Reion
 * @date 2023-12-14
 **/
@Slf4j
@Service
@CatchAndLog
public class WalletServiceImpl implements IWalletService {

    @Resource
    private WalletCreateCmdExe walletCreateCmdExe;
    @Resource
    private WalletRechargeCmdExe walletRechargeCmdExe;
    @Resource
    private WalletQryExe walletQryExe;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SingleResponse<Long> createWallet(WalletCreateCmd cmd) {
        return walletCreateCmdExe.execute(cmd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateWallet(WalletRechargeCmd walletRechargeCmd) {
        return walletRechargeCmdExe.execute(walletRechargeCmd);
    }

    @Override
    public WalletDTO getWalletForUpdate(Long userId) {
        return walletQryExe.execute(userId);
    }
}

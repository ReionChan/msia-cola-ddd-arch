package io.github.reionchan.payment.command;

import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.orders.dto.OrderCreationDTO;
import io.github.reionchan.payment.assembler.PaymentAssembler;
import io.github.reionchan.payment.dto.command.PaymentCreateCmd;
import io.github.reionchan.payment.extentionpoint.PaymentCreateExtPt;
import io.github.reionchan.payment.model.entity.Payment;
import io.github.reionchan.payment.model.entity.Wallet;
import io.github.reionchan.payment.model.vo.PayPlatform;
import io.github.reionchan.payment.repository.IPaymentRepository;
import io.github.reionchan.payment.repository.IWalletRepository;
import io.github.reionchan.orders.api.IOrderRpc;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;
import static io.github.reionchan.payment.model.vo.PaymentStatus.PAYING;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class PaymentCreateCmdExe {

    @Resource
    private IPaymentRepository paymentRepository;
    @Resource
    private IWalletRepository walletRepository;
    @Resource
    private PaymentAssembler paymentAssembler;
    @Resource
    private ExtensionExecutor extensionExecutor;
    @Resource
    private IOrderRpc orderRpc;

    public SingleResponse<Long> execute(PaymentCreateCmd cmd) {
        // 校验支付单不存在
        extensionExecutor.executeVoid(PaymentCreateExtPt.class, PaymentCreateCmd.getDefault(), chk -> chk.validate(cmd));

        // 业务
        // RPC 查询订单
        SingleResponse<OrderCreationDTO> response = orderRpc.getOrder(cmd.getOrderId());
        Assert.isTrue(response.isSuccess(), "无效订单");
        cmd.setUserId(response.getData().getUserId());
        cmd.setTotalPrice(response.getData().getTotalPrice());

        // 采用 select for update 方式锁定用户钱包，防止并发更新
        Wallet wallet = walletRepository.getWalletForUpdate(cmd.getUserId());
        Assert.isTrue(isNotEmpty(wallet), "用户钱包不存在");
        Assert.isTrue(wallet.getAvailableBalance().compareTo(cmd.getTotalPrice()) >= 0, "用户余额不足");
        wallet.setAvailableBalance(wallet.getAvailableBalance().subtract(cmd.getTotalPrice()));
        wallet.setBlockedBalance(wallet.getBlockedBalance().add(cmd.getTotalPrice()));
        Assert.isTrue(walletRepository.updateById(wallet), "更新用户钱包失败");
        // 创建支付
        Payment payment = paymentAssembler.toEntity(cmd);
        payment.setAmount(cmd.getTotalPrice());
        payment.setPayPlatform(PayPlatform.OWN);
        payment.setPaymentStatus(PAYING);
        Assert.isTrue(paymentRepository.save(payment), "创建支付记录失败");
        log.info("用户：{} 支付订单：{} 金额：{} 支付单号：{}",
                payment.getUserId(), payment.getOrderId(), payment.getAmount(), payment.getId());
        return SingleResponse.of(payment.getId());
    }
}

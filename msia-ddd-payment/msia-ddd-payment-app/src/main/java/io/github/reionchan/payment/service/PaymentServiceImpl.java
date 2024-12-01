package io.github.reionchan.payment.service;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.Assert;
import io.github.reionchan.orders.api.IOrderRpc;
import io.github.reionchan.payment.api.IPaymentService;
import io.github.reionchan.payment.assembler.PaymentAssembler;
import io.github.reionchan.payment.command.PaymentCreateCmdExe;
import io.github.reionchan.payment.command.query.PaymentToPayQryExe;
import io.github.reionchan.payment.dto.PaymentDTO;
import io.github.reionchan.payment.dto.command.PaymentCreateCmd;
import io.github.reionchan.payment.model.entity.Wallet;
import io.github.reionchan.payment.repository.IPaymentRepository;
import io.github.reionchan.payment.repository.IWalletRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;
import static io.github.reionchan.orders.consts.OrderStatus.PAID;
import static io.github.reionchan.payment.consts.PaymentStatus.PAYED;

/**
 * 支付服务实现类
 *
 * @author Reion
 * @date 2023-12-16
 **/
@Slf4j
@Service
@CatchAndLog
public class PaymentServiceImpl implements IPaymentService {

    @Resource
    private IWalletRepository walletRepository;
    @Resource
    private IPaymentRepository paymentRepository;
    @Resource
    private PaymentCreateCmdExe paymentCreateCmdExe;
    @Resource
    private PaymentToPayQryExe paymentToPayQryExe;
    @Resource
    private PaymentAssembler paymentAssembler;
    @Resource
    private IOrderRpc orderRpc;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SingleResponse<Long> createPayment(PaymentCreateCmd paymentCreateCmd) {
        return paymentCreateCmdExe.execute(paymentCreateCmd);
    }

    @Override
    public List<PaymentDTO> getPayingList() {
        return paymentToPayQryExe.execute();
    }


    @Transactional(rollbackFor = Exception.class)
    public void payedInTransaction(PaymentDTO payment) throws RuntimeException {
        // 采用 select for update 方式锁定用户钱包，防止并发更新
        Wallet wallet = walletRepository.getWalletForUpdate(payment.getUserId());
        Assert.isTrue(isNotEmpty(wallet), "用户钱包不存在");
        Assert.isTrue(wallet.getBlockedBalance().compareTo(payment.getAmount()) >= 0,
            String.format("用户：%s 冻结金额不足：%s 小于所需金额：%s", payment.getUserId(), wallet.getBlockedBalance(),
                payment.getAmount()));
        wallet.setBlockedBalance(wallet.getBlockedBalance().subtract(payment.getAmount()));

        payment.setStatus(PAYED.getValue());
        Assert.isTrue(walletRepository.updateById(wallet) && paymentRepository.updateById(paymentAssembler.toEntity(payment)), "交易更新失败");

        Response orderStatusR = orderRpc.modify(payment.getOrderId(), PAID.getValue());
        Assert.isTrue(orderStatusR.isSuccess(), "订单状态修改失败");
        log.info("支付单号：{} 完成冻结金额：{} 划扣！", payment.getId(), payment.getAmount());
    }
}

package io.github.reionchan.payment.extentionpoint.extention;

import com.alibaba.cola.extension.Extension;
import io.github.reionchan.payment.dto.command.PaymentCreateCmd;
import io.github.reionchan.payment.extentionpoint.PaymentCreateExtPt;
import io.github.reionchan.payment.repository.IPaymentRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import static io.github.reionchan.payment.consts.PaymentBizScenarioCst.PAYMENT_BIZ_ID;
import static io.github.reionchan.payment.consts.PaymentBizScenarioCst.PAYMENT_PAY_USE_CASE;

/**
 * @author Reion
 * @date 2024-11-20
 **/
@Slf4j
@Extension(bizId = PAYMENT_BIZ_ID, useCase = PAYMENT_PAY_USE_CASE)
public class PaymentExistValidatorExt implements PaymentCreateExtPt {

    @Resource
    private IPaymentRepository paymentRepository;

    @Override
    public void validate(PaymentCreateCmd cmd) {
        Assert.isTrue(!paymentRepository.existsPayment(cmd), "订单支付中或已支付");
    }
}

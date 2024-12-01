package io.github.reionchan.payment.extentionpoint;

import com.alibaba.cola.extension.ExtensionPointI;
import io.github.reionchan.payment.dto.command.PaymentCreateCmd;

/**
 * 支付创建前扩展点
 *
 * @author Reion
 * @date 2024-11-20
 **/
public interface PaymentCreateExtPt extends ExtensionPointI {

    void validate(PaymentCreateCmd cmd);
}

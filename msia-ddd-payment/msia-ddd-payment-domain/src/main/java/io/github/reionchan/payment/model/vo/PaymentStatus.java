package io.github.reionchan.payment.model.vo;

import com.alibaba.cola.exception.Assert;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 支付平台
 *
 * @author Reion
 * @date 2023-12-17
 **/
public enum PaymentStatus {

    /**
     * 自建
     */
    PAYING((byte) 0, "支付中"),
    /**
     * 支付宝
     */
    PAYED((byte) 1, "已支付");

    byte value;
    String name;

    PaymentStatus(byte value, String name) {
        this.value = value;
        this.name = name;
    }

    public byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static PaymentStatus of(Byte value) {
        Assert.isTrue(Objects.nonNull(value), "支付状态值不能为空");
        Optional<PaymentStatus> paymentStatus = Arrays.stream(PaymentStatus.values()).filter(s -> s.getValue() == value).findFirst();
        Assert.isTrue(paymentStatus.isPresent(), "支付状态值不合法");
        return paymentStatus.get();
    }
}

package io.github.reionchan.payment.model.vo;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 支付平台
 *
 * @author Reion
 * @date 2023-12-17
 **/
public enum PayPlatform {

    /**
     * 自建
     */
    OWN((byte) 0, "own"),
    /**
     * 支付宝
     */
    ALIPAY((byte) 1, "alipay"),
    /**
     * 微信
     */
    WECHAT((byte) 2, "wechat"),
    /**
     * 银联
     */
    UNIONPAY((byte) 3, "unionpay");

    byte value;
    String name;

    PayPlatform(byte value, String name) {
        this.value = value;
        this.name = name;
    }

    public byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static PayPlatform of(Byte value) {
        Assert.isTrue(Objects.nonNull(value), "支付平台值不能为空");
        Optional<PayPlatform> payPlatform = Arrays.stream(PayPlatform.values()).filter(s -> s.getValue() == value).findFirst();
        Assert.isTrue(payPlatform.isPresent(), "支付平台值不合法");
        return payPlatform.get();
    }
}

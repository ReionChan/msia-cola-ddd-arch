package io.github.reionchan.payment.model.entity;

import com.alibaba.cola.domain.Entity;
import com.alibaba.cola.exception.Assert;
import io.github.reionchan.payment.model.vo.PayPlatform;
import io.github.reionchan.payment.model.vo.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付持久化类
 *
 * @author Reion
 * @date 2023-04-25
 **/
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "orderId", "userId"})
public class Payment implements Serializable {

    private Long id;

    private Long userId;

    private Long orderId;

    private BigDecimal amount;

    private PaymentStatus paymentStatus;

    private PayPlatform payPlatform;

    private String outerId;

    public void getStatus(Byte status) {
        Assert.notNull(status, "支付状态值为空");
        this.paymentStatus = PaymentStatus.of(status);
    }

    public void getPlatform(Byte platform) {
        Assert.notNull(platform, "支付平台值为空");
        this.payPlatform = PayPlatform.of(platform);
    }

    public Byte getPlatform() {
        Assert.notNull(this.payPlatform, "支付平台值为空");
        return this.payPlatform.getValue();
    }

    public Byte getStatus() {
        Assert.notNull(this.paymentStatus, "支付状态值为空");
        return this.paymentStatus.getValue();
    }
}

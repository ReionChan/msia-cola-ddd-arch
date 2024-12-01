package io.github.reionchan.orders.model.entity;

import com.alibaba.cola.domain.Entity;
import com.alibaba.cola.exception.Assert;
import io.github.reionchan.orders.model.vo.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单持久化类
 *
 * @author Reion
 * @date 2023-04-25
 **/
@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "userId"})
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Integer totalAmount;

    private OrderStatus orderStatus;

    private BigDecimal totalPrice;

    public void getOrderStatus(Byte status) {
        Assert.notNull(status, "订单状态不能为空");
        this.orderStatus = OrderStatus.of(status);
    }

    public Byte getStatus() {
        Assert.notNull(this.orderStatus, "订单状态为空");
        return orderStatus.getValue();
    }
}

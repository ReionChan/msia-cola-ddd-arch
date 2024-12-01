package io.github.reionchan.orders.model.vo;

import com.alibaba.cola.exception.Assert;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 订单状态枚举
 *
 * @author Reion
 * @date 2023-12-15
 **/
public enum OrderStatus {

    UNPAID((byte) 0, "未支付"),
    PAYING((byte) 1, "支付中"),
    PAID((byte) 2, "已支付"),
    CANCEL((byte) 3, "已取消"),
    DELIVERED((byte) 4, "已发货"),
    RECEIVED((byte) 5, "已签收"),
    RETURNED((byte) 6, "已退货"),
    RETURNED_REFUND((byte) 7, "已退款"),
    DELETED((byte) 8, "已删除"),
    OUT_OF_STOCK((byte) 9, "库存不足");

    private Byte value;
    private String name;

    OrderStatus(Byte value, String name) {
        this.value = value;
        this.name = name;
    }
    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static OrderStatus of(Byte value) {
        Assert.isTrue(Objects.nonNull(value), "订单状态值不能为空");
        Optional<OrderStatus> orderStatus = Arrays.stream(OrderStatus.values()).filter(s -> s.getValue() == value).findFirst();
        Assert.isTrue(orderStatus.isPresent(), "订单状态值不合法");
        return orderStatus.get();
    }
}

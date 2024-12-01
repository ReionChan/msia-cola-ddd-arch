package io.github.reionchan.orders.extentionpoint;

import com.alibaba.cola.extension.ExtensionPointI;

/**
 * 支付创建前扩展点
 *
 * @author Reion
 * @date 2024-11-20
 **/
public interface OrderOwnerExtPt extends ExtensionPointI {

    void validate(Long orderOwnerId);
}

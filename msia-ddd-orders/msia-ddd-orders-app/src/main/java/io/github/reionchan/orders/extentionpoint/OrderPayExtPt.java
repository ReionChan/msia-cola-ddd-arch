package io.github.reionchan.orders.extentionpoint;

import com.alibaba.cola.extension.ExtensionPointI;
import io.github.reionchan.core.model.entity.MQMessage;

/**
 * 用户注册扩展点
 *
 * @author Reion
 * @date 2024-11-20
 **/
public interface OrderPayExtPt extends ExtensionPointI {
    void notify(MQMessage message);
}

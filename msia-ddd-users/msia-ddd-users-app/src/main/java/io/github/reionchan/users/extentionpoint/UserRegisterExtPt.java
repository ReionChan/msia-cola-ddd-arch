package io.github.reionchan.users.extentionpoint;

import com.alibaba.cola.extension.ExtensionPointI;
import io.github.reionchan.core.model.entity.MQMessage;
import io.github.reionchan.users.dto.command.UserAddCmd;

/**
 * 用户注册扩展点
 *
 * @author Reion
 * @date 2024-11-20
 **/
public interface UserRegisterExtPt extends ExtensionPointI {
    void validate(UserAddCmd cmd);

    void notify(MQMessage message);
}

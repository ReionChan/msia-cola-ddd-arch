package io.github.reionchan.users.model.vo;

import com.alibaba.cola.exception.Assert;

import java.util.Arrays;
import java.util.Optional;

/**
 * 用户状态枚举
 *
 * @author Reion
 * @date 2023-12-15
 **/
public enum UserStatus {

    INIT((byte)-1, "初始化"),
    ENABLED((byte) 0, "启用"),
    DISABLED((byte) 1, "停用");

    private Byte value;
    private String name;

    UserStatus(Byte value, String name) {
        this.value = value;
        this.name = name;
    }
    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static UserStatus of(Byte value) {
        Assert.notNull(value, "用户状态值不能为空");
        Optional<UserStatus> userStatus = Arrays.stream(UserStatus.values()).filter(s -> s.getValue().equals(value)).findFirst();
        Assert.isTrue(userStatus.isPresent(), "用户状态值不合法");
        return userStatus.get();
    }
}

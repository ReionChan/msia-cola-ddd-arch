package io.github.reionchan.users.model.vo;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;


/**
 * 角色状态枚举
 *
 * @author Reion
 * @date 2023-12-15
 **/
public enum RoleStatus {

    ENABLED((byte) 0, "启用"),
    DISABLED((byte) 1, "停用");

    private Byte value;
    private String name;

    RoleStatus(Byte value, String name) {
        this.value = value;
        this.name = name;
    }
    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static RoleStatus of(Byte value) {
        Assert.isTrue(Objects.nonNull(value), "角色状态值不能为空");
        Optional<RoleStatus> userStatus = Arrays.stream(RoleStatus.values()).filter(s -> s.getValue().equals(value)).findFirst();
        Assert.isTrue(userStatus.isPresent(), "角色状态值不合法");
        return userStatus.get();
    }
}

package io.github.reionchan.mq.consts;

/**
 * 消息状态枚举
 *
 * @author Reion
 * @date 2023-12-15
 **/
public enum MessageStatus {

    NEW((byte) 0, "新建"),
    SENT((byte) 1, "已发送"),
    SENT_ERROR((byte) 2, "错误抵达"),
    DELIVERED((byte) 3, "已抵达");

    private Byte value;
    private String name;

    MessageStatus(Byte value, String name) {
        this.value = value;
        this.name = name;
    }
    public Byte getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}

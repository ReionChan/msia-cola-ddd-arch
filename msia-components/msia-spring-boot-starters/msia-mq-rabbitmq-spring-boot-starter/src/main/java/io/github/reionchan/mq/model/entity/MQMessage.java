package io.github.reionchan.mq.model.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户持久化类
 *
 * @author Reion
 * @date 2023-04-25
 **/
@Data
@Builder
@EqualsAndHashCode(of = {"messageId"})
public class MQMessage implements Serializable {

    static final long serialVersionUID = 1L;

    private String messageId;

    private String content;

    private String toExchange;

    private String routingKey;

    private String classType;

    private Byte messageStatus;

    private String remark;

    private Date createTime;

    private Date updateTime;
}

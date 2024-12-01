package io.github.reionchan.core.database.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户持久化类
 *
 * @author Reion
 * @date 2023-04-25
 **/
@Data
@EqualsAndHashCode(of = {"messageId"})
@TableName("mq_message")
public class MQMessageDO implements Serializable {

    static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private String messageId;

    private String content;

    private String toExchange;

    private String routingKey;

    private String classType;

    private Byte messageStatus;

    private String remark;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;
}

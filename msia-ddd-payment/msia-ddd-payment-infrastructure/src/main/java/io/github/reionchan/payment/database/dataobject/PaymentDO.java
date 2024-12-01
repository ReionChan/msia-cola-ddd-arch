package io.github.reionchan.payment.database.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付持久化类
 *
 * @author Reion
 * @date 2023-04-25
 **/
@Data
@EqualsAndHashCode(of = {"id", "orderId", "userId"})
@TableName("payment")
public class PaymentDO implements Serializable {

    static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long orderId;

    private BigDecimal amount;

    private Byte status;

    private Byte platform;

    private String outerId;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;
}

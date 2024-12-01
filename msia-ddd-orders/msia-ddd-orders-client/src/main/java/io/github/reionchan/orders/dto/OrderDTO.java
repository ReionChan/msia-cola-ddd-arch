package io.github.reionchan.orders.dto;

import com.alibaba.cola.dto.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单持久化类
 *
 * @author Reion
 * @date 2023-04-25
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "userId"})
public class OrderDTO extends DTO {

    private Long id;

    private Long userId;

    private Integer totalAmount;

    private Byte status;

    private BigDecimal totalPrice;

    @Schema(title = "创建时间，格式：yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @Schema(title = "修改时间，格式：yyyy-MM-dd hh:mm:ss")
    private Date updateTime;
}

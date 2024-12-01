package io.github.reionchan.orders.dto;

import com.alibaba.cola.dto.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单详情数据传输对象
 *
 * @author Reion
 * @date 2023-12-17
 **/
@Data
@EqualsAndHashCode(of = {"id", "orderId", "productId"})
@Schema(name = "OrderDetailDTO", description = "订单详情数据传输对象")
public class OrderDetailDTO extends DTO {

    @Positive(message = "id 必须大于 0")
    @Schema(title = "订单详情ID", description = "订单详情唯一ID")
    private Long id;

    @Positive(message = "orderId 必须大于 0")
    @Schema(title = "订单ID", description = "订单唯一ID")
    private Long orderId;

    @NotNull(message = "productId 不能为空")
    @Positive(message = "productId 必须大于 0")
    @Schema(title = "产品ID", description = "产品唯一ID")
    private Long productId;

    @Size(min = 1, max = 32, message = "产品名称长度至少 1 个字符，至多 32 个字符")
    @Schema(title = "产品名称", description = "长度8~32",  minLength = 1, maxLength = 32)
    private String productName;

    @DecimalMin(value = "0.01", message = "产品单价必须不小于 0.01")
    @Digits(integer = 10, fraction = 2, message = "整数最长 10 位，小数最长 2 位")
    @Schema(title = "产品单价", description = "整数最长 10 位，小数最长 2 位，必须不小于 0.01，单位：元")
    private BigDecimal price;

    @NotNull(message = "购买数量不能为空")
    @Positive(message = "购买数量必须大于 0")
    @Schema(title = "购买数量", description = "购买数量必须大于 0")
    private Integer amount;

    @DecimalMin(value = "0.01", message = "产品价格小计必须不小于 0.01")
    @Digits(integer = 10, fraction = 2, message = "整数最长 10 位，小数最长 2 位")
    @Schema(title = "产品价格小计", description = "整数最长 10 位，小数最长 2 位，必须不小于 0.01，单位：元")
    private BigDecimal sumPrice;

    @Schema(title = "创建时间，格式：yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @Schema(title = "修改时间，格式：yyyy-MM-dd hh:mm:ss")
    private Date updateTime;
}

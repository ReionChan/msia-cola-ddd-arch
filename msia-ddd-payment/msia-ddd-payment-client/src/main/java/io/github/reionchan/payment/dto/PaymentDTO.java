package io.github.reionchan.payment.dto;

import com.alibaba.cola.dto.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 支付数据传输对象
 *
 * @author Reion
 * @date 2023-12-17
 **/
@Data
@EqualsAndHashCode(of = {"id", "userId", "orderId"})
@Schema(name = "PaymentDto", description = "支付数据传输对象")
public class PaymentDTO extends DTO {

    @NotNull(message = "支付ID 不能为空")
    @Positive(message = "id 必须大于 0")
    @Schema(title = "支付ID", description = "支付唯一ID")
    private Long id;

    @NotNull(message = "用户ID 不能为空")
    @Positive(message = "userId 必须大于 0")
    @Schema(title = "用户ID", description = "用户唯一ID")
    private Long userId;

    @NotNull(message = "订单ID 不能为空")
    @Positive(message = "orderId 必须大于 0")
    @Schema(title = "订单ID", description = "订单唯一ID")
    private Long orderId;

    @NotNull(message = "支付金额不能为空")
    @DecimalMin(value = "0.01", message = "支付金额必须不小于 0.01")
    @Digits(integer = 10, fraction = 2, message = "整数最长 10 位，小数最长 2 位")
    @Schema(title = "支付金额", description = "整数最长 10 位，小数最长 2 位，必须不小于 0.01，单位：元")
    private BigDecimal amount;

    @Min(value = 0, message = "状态值不合法，最小 0")
    @Max(value = 127, message = "状态值不合法，最大 127")
    @Schema(type = "integer", title = "支付状态：0、null-未支付 1-已支付")
    private Byte status;

    private Byte platform;
}

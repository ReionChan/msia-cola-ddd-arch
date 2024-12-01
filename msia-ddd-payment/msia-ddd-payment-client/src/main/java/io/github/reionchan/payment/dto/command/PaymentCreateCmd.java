package io.github.reionchan.payment.dto.command;

import com.alibaba.cola.dto.Command;
import com.alibaba.cola.extension.BizScenario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

import static io.github.reionchan.payment.consts.PaymentBizScenarioCst.*;

/**
 * 支付信息数据传输对象
 *
 * @author Reion
 * @date 2023-12-17
 **/
@Data
@Builder
@Schema(name = "PaymentCreateCmd", description = "支付信息数据传输对象")
public class PaymentCreateCmd extends Command {

    private static final long serialVersionUID = 1L;

    private BizScenario bizScenario;

    @NotNull(message = "用户ID 不能为空")
    @Positive(message = "userId 必须大于 0")
    @Schema(title = "用户ID", description = "用户唯一ID")
    private Long userId;

    @NotNull(message = "订单ID 不能为空")
    @Positive(message = "orderId 必须大于 0")
    @Schema(title = "订单ID", description = "订单唯一ID")
    private Long orderId;

    @NotNull(message = "支付金额不能为空")
    @DecimalMin(value = "0.01", message = "订单购买总价格必须不小于 0.01")
    @Digits(integer = 10, fraction = 2, message = "整数最长 10 位，小数最长 2 位")
    @Schema(title = "订单购买总价格", description = "整数最长 10 位，小数最长 2 位，必须不小于 0.01，单位：元")
    private BigDecimal totalPrice;

    public static BizScenario getDefault() {
        return BizScenario.valueOf(PAYMENT_BIZ_ID, PAYMENT_PAY_USE_CASE, PAYMENT_PAY_CREATE_SCENARIO);
    }
}

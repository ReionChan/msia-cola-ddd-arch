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

import java.math.BigDecimal;

import static io.github.reionchan.payment.consts.PaymentBizScenarioCst.*;

/**
 * 钱包数据传输对象
 *
 * @author Reion
 * @date 2023-12-17
 **/
@Data
@Builder
@Schema(name = "WalletDto", description = "钱包数据传输对象")
public class WalletRechargeCmd extends Command {

    private static final long serialVersionUID = 1L;

    private BizScenario bizScenario;

    @NotNull(message = "钱包ID 不能为空")
    @Positive(message = "id 必须大于 0")
    @Schema(title = "钱包ID", description = "支付唯一ID")
    private Long id;

    @NotNull(message = "充值金额不能为空")
    @DecimalMin(value = "1.00", message = "充值金额至少 1.00")
    @Digits(integer = 10, fraction = 2, message = "整数最长 10 位，小数最长 2 位")
    @Schema(title = "充值金额", description = "整数最长 10 位，小数最长 2 位 单位：元")
    private BigDecimal rechargeAmount;

    public static BizScenario getDefault() {
        return BizScenario.valueOf(PAYMENT_BIZ_ID, PAYMENT_WALLET_USE_CASE, PAYMENT_WALLET_RECHARGE_SCENARIO);
    }
}

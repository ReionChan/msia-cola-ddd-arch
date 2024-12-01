package io.github.reionchan.payment.dto.command;

import com.alibaba.cola.dto.Command;
import com.alibaba.cola.extension.BizScenario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

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
public class WalletCreateCmd extends Command {

    private static final long serialVersionUID = 1L;

    private BizScenario bizScenario;

    @NotNull(message = "用户ID 不能为空")
    @Positive(message = "userId 必须大于 0")
    @Schema(title = "用户ID", description = "用户唯一ID")
    private Long userId;

    public static BizScenario getDefault() {
        return BizScenario.valueOf(PAYMENT_BIZ_ID, PAYMENT_WALLET_USE_CASE, PAYMENT_WALLET_CREATE_SCENARIO);
    }
}

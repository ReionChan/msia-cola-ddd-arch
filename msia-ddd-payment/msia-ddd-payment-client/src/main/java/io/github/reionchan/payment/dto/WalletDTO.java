package io.github.reionchan.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 钱包数据传输对象
 *
 * @author Reion
 * @date 2023-12-17
 **/
@Data
@EqualsAndHashCode(of = {"id", "userId"})
@Schema(name = "WalletDto", description = "钱包数据传输对象")
public class WalletDTO implements Serializable {
    static final long serialVersionUID = 1L;

    @NotNull(message = "钱包ID 不能为空")
    @Positive(message = "id 必须大于 0")
    @Schema(title = "钱包ID", description = "支付唯一ID")
    private Long id;

    @NotNull(message = "用户ID 不能为空")
    @Positive(message = "userId 必须大于 0")
    @Schema(title = "用户ID", description = "用户唯一ID")
    private Long userId;

    @DecimalMin(value = "0.00", message = "可用金额非负数")
    @Digits(integer = 10, fraction = 2, message = "整数最长 10 位，小数最长 2 位")
    @Schema(title = "可用金额", description = "整数最长 10 位，小数最长 2 位 单位：元")
    private BigDecimal availableBalance;

    @NotNull(message = "充值金额不能为空")
    @DecimalMin(value = "1.00", message = "充值金额至少 1.00")
    @Digits(integer = 10, fraction = 2, message = "整数最长 10 位，小数最长 2 位")
    @Schema(title = "充值金额", description = "整数最长 10 位，小数最长 2 位 单位：元")
    private BigDecimal rechargeAmount;

    @DecimalMin(value = "0.00", message = "冻结金额非负数")
    @Digits(integer = 10, fraction = 2, message = "整数最长 10 位，小数最长 2 位")
    @Schema(title = "冻结金额", description = "整数最长 10 位，小数最长 2 位 单位：元")
    private BigDecimal blockedBalance;

    @Schema(title = "创建时间，格式：yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @Schema(title = "修改时间，格式：yyyy-MM-dd hh:mm:ss")
    private Date updateTime;
}

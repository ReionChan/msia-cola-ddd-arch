package io.github.reionchan.orders.dto.command;

import com.alibaba.cola.dto.Command;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单详情数据传输对象
 *
 * @author Reion
 * @date 2023-12-17
 **/
@Data
@EqualsAndHashCode(of = {"productId"})
@Schema(name = "OrderDetailCreationCmd", description = "订单详情数据传输对象")
public class OrderDetailCreationCmd extends Command {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "productId 不能为空")
    @Positive(message = "productId 必须大于 0")
    @Schema(title = "产品ID", description = "产品唯一ID")
    private Long productId;

    @NotNull(message = "购买数量不能为空")
    @Positive(message = "购买数量必须大于 0")
    @Schema(title = "购买数量", description = "购买数量必须大于 0")
    private Integer amount;
}

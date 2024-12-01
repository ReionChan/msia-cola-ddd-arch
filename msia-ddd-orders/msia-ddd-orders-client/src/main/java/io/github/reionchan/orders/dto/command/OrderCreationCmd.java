package io.github.reionchan.orders.dto.command;

import com.alibaba.cola.dto.Command;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * 订单创建数据传输对象
 *
 * @author Reion
 * @date 2023-12-17
 **/
@Data
@Schema(name = "OrderCreationCmd", description = "订单创建数据传输对象")
public class OrderCreationCmd extends Command {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "用户ID 不能为空")
    @Positive(message = "userId 必须大于 0")
    @Schema(title = "用户ID", description = "用户唯一ID")
    private Long userId;

    @Valid // 级联校验
    @NotNull(message = "订单商品列表不能为空")
    @Size(min = 1, message = "订单至少包含一种商品")
    private Set<OrderDetailCreationCmd> orderDetails;
}

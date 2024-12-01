package io.github.reionchan.orders.dto;

import com.alibaba.cola.dto.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * 订单创建数据传输对象
 *
 * @author Reion
 * @date 2023-12-17
 **/
@Data
@Schema(name = "OrderCreationDto", description = "订单创建数据传输对象")
public class OrderCreationDTO extends DTO {
    static final String REX_DATETIME = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])";

    @NotNull(message = "订单ID 不能为空")
    @Positive(message = "id 必须大于 0")
    @Schema(title = "订单ID", description = "订单唯一ID")
    private Long id;

    @NotNull(message = "用户ID 不能为空")
    @Positive(message = "userId 必须大于 0")
    @Schema(title = "用户ID", description = "用户唯一ID")
    private Long userId;

    @Valid // 级联校验
    @NotNull(message = "订单商品列表不能为空")
    @Size(min = 1, message = "订单至少包含一种商品")
    private Set<OrderDetailDTO> orderDetails;

    @Positive(message = "订单购买总数量必须大于 0")
    @Schema(title = "订单购买总数量", description = "订单购买总数量必须大于 0")
    private Integer totalAmount;

    @DecimalMin(value = "0.01", message = "订单购买总价格必须不小于 0.01")
    @Digits(integer = 10, fraction = 2, message = "整数最长 10 位，小数最长 2 位")
    @Schema(title = "订单购买总价格", description = "整数最长 10 位，小数最长 2 位，必须不小于 0.01，单位：元")
    private BigDecimal totalPrice;

    @Min(value = 0, message = "状态值不合法，最小 0")
    @Max(value = 7, message = "状态值不合法，最大 7")
    @Schema(type = "integer", title = "订单状态：0、null-未支付 1-已支付 2-已取消 3-已发货 4-已签收 5-已退货 6-已退款 7-已删除")
    private Byte status;

    @Schema(title = "创建时间，格式：yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    @Schema(title = "修改时间，格式：yyyy-MM-dd hh:mm:ss")
    private Date updateTime;
}

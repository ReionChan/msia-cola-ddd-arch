package io.github.reionchan.products.dto.clientobject;

import com.alibaba.cola.dto.ClientObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 产品库存数据传输对象
 *
 * @author Reion
 * @date 2023-12-14
 **/
@Data
@Schema(name = "ProductStockWebCO", description = "产品库存数据传输对象")
public class ProductStockWebCO extends ClientObject {

    private static final long serialVersionUID = 1L;

    @Positive(message = "id 必须大于 0")
    @Schema(title = "产品ID", description = "产品唯一ID")
    private Long id;

    @Min(value = 1, message = "产品名称长度最小 1")
    @Max(value = 32, message = "产品名称长度最大 32")
    @Schema(title = "产品名称", description = "长度8~32",  minLength = 1, maxLength = 32)
    private String name;

    @DecimalMin(value = "0.01", message = "产品价格必须不小于 0.01")
    @Digits(integer = 10, fraction = 2, message = "整数最长 10 位，小数最长 2 位")
    @Schema(title = "产品价格", description = "整数最长 10 位，小数最长 2 位，必须不小于 0.01，单位：元")
    private BigDecimal price;

    @Pattern(regexp = "[0-9]{6,32}", message = "产品条码长度6~32的数字字符串")
    @Schema(title = "产品条码", description = "长度6~32的数字字符串",  minLength = 6, maxLength = 32)
    private String barCode;

    @PositiveOrZero(message = "库存数量必须大于等于 0")
    @Schema(title = "库存数量", description = "库存必须大于等于 0")
    private Integer amount;
}

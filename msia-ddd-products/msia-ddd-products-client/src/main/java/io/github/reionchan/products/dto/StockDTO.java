package io.github.reionchan.products.dto;

import com.alibaba.cola.dto.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 库存数据传输对象
 *
 * @author Reion
 * @date 2023-12-14
 **/
@Data
@EqualsAndHashCode(of = {"id", "productId"})
@Schema(name = "StockDto", description = "库存数据传输对象")
public class StockDTO extends DTO {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "id 不能为空")
    @Positive(message = "id 必须大于 0")
    @Schema(title = "库存ID", description = "库存唯一ID")
    private Long id;

    @NotNull(message = "productId 不能为空")
    @Positive(message = "productId 必须大于 0")
    @Schema(title = "产品ID", description = "产品唯一ID")
    private Long productId;

    @Min(value = 1, message = "产品名称长度最小 1")
    @Max(value = 32, message = "产品名称长度最大 32")
    @Schema(title = "产品名称", description = "长度8~32",  minLength = 1, maxLength = 32)
    private String name;

    @NotNull(message = "库存数量不能为空")
    @PositiveOrZero(message = "库存数量必须大于等于 0")
    @Schema(title = "库存数量", description = "库存必须大于等于 0")
    private Integer amount;
}

package io.github.reionchan.products.dto.command;

import com.alibaba.cola.dto.Command;
import com.alibaba.cola.extension.BizScenario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static io.github.reionchan.products.consts.ProductsBizScenarioCst.*;

/**
 * 库存数据传输对象
 *
 * @author Reion
 * @date 2023-12-14
 **/
@Data
@Schema(name = "StockCreateCmd", description = "库存数据传输对象")
public class StockCreateCmd extends Command {

    private static final long serialVersionUID = 1L;

    private BizScenario bizScenario = BizScenario.valueOf(PRODUCTS_BIZ_ID, PRODUCTS_STOCK_CREATE_USE_CASE);

    @NotNull(message = "productId 不能为空")
    @Positive(message = "productId 必须大于 0")
    @Schema(title = "产品ID", description = "产品唯一ID")
    private Long productId;

    @NotNull(message = "库存数量不能为空")
    @PositiveOrZero(message = "库存数量必须大于等于 0")
    @Schema(title = "库存数量", description = "库存必须大于等于 0")
    private Integer amount;
}

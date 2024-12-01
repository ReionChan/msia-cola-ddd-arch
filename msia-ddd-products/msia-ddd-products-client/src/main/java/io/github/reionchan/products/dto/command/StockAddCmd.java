package io.github.reionchan.products.dto.command;

import com.alibaba.cola.dto.Command;
import com.alibaba.cola.extension.BizScenario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static io.github.reionchan.products.consts.ProductsBizScenarioCst.PRODUCTS_BIZ_ID;
import static io.github.reionchan.products.consts.ProductsBizScenarioCst.PRODUCTS_STOCK_MODIFY_USE_CASE;

/**
 * 库存数据传输对象
 *
 * @author Reion
 * @date 2023-12-14
 **/
@Data
@EqualsAndHashCode(of = {"id", "productId"})
@Schema(name = "StockAddCmd", description = "库存数据传输对象")
public class StockAddCmd extends Command {

    private static final long serialVersionUID = 1L;

    private BizScenario bizScenario = BizScenario.valueOf(PRODUCTS_BIZ_ID, PRODUCTS_STOCK_MODIFY_USE_CASE);

    @NotNull(message = "id 不能为空")
    @Positive(message = "id 必须大于 0")
    @Schema(title = "库存ID", description = "库存唯一ID")
    private Long id;

    @NotNull(message = "库存数量不能为空")
    @Positive(message = "库存数量必须大于 0")
    @Schema(title = "库存数量", description = "库存必须大于 0")
    private Integer amount;
}

package io.github.reionchan.products.web;

import com.alibaba.cola.dto.Response;
import io.github.reionchan.products.api.IStockService;
import io.github.reionchan.products.dto.command.StockAddCmd;
import io.github.reionchan.products.dto.command.StockCreateCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 库存控制器
 *
 * @author Reion
 * @date 2023-12-15
 **/
@Slf4j
@RestController
@Tag(name = "StockController", description = "库存服务请求端点")
public class StockController {

    private IStockService stockService;

    public StockController(IStockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/stock")
    @Operation(summary = "新增库存信息", description = "新增库存信息接口",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "StockCreateCmd 传输对象",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    )
    @PreAuthorize("hasRole('admin')")
    public Response newStock(@RequestBody @Validated StockCreateCmd cmd) {
        return stockService.newStock(cmd);
    }

    @PutMapping("/stock")
    @Operation(summary = "追加库存信息", description = "追加库存接口",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "StockAddCmd 传输对象",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    )
    @PreAuthorize("hasRole('admin')")
    public Response addStock(@RequestBody @Validated StockAddCmd cmd) {
        return stockService.addStock(cmd);
    }
}

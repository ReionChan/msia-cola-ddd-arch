package io.github.reionchan.products.web;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import io.github.reionchan.core.dto.PageQry;
import io.github.reionchan.products.api.IProductService;
import io.github.reionchan.products.dto.clientobject.ProductStockWebCO;
import io.github.reionchan.products.dto.clientobject.ProductWebCO;
import io.github.reionchan.products.dto.command.ProductCreateCmd;
import io.github.reionchan.products.dto.command.ProductModifyCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 产品控制器
 *
 * @author Reion
 * @date 2023-12-15
 **/
@Slf4j
@RestController
@Tag(name = "ProductController", description = "产品服务请求端点")
public class ProductController {

    private IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/product")
    @Operation(summary = "添加产品", description = "新增产品接口",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "ProductCreateCmd 传输对象",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    )
    @PreAuthorize("hasRole('admin')")
    public Response add(@RequestBody @Validated ProductCreateCmd cmd) {
        return productService.add(cmd);
    }

    @PutMapping("/product")
    @Operation(summary = "修改产品", description = "修改产品接口",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "ProductModifyCmd 对象",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    )
    @PreAuthorize("hasRole('admin')")
    public Response modify(@RequestBody @Validated ProductModifyCmd cmd) {
        return productService.modify(cmd);
    }

    @GetMapping("/products")
    @Operation(summary = "产品库存列表", description = "显示产品列表并显示库存信息")
    @PreAuthorize("hasRole('user')")
    public MultiResponse<ProductWebCO> getProductsByIds(@RequestParam("ids")
        @Validated @Pattern(regexp = "^[1-9][0-9,]*$", message = "仅支持逗号隔开的非零开头的数字") String ids) {
        Set<Long> idSet = Arrays.stream(ids.split(",")).mapToLong(Long::parseLong)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);
        return productService.getProductsByIds(idSet);
    }

    @GetMapping("/products/stock")
    @Operation(summary = "产品库存列表", description = "显示产品列表并显示库存信息")
    @PreAuthorize("hasRole('admin')")
    public PageResponse<ProductStockWebCO> getProductsWithStock(PageQry query) {
        return productService.getProductsWithStock(query);
    }
}

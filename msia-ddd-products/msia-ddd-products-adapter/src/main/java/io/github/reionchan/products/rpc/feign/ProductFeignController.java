package io.github.reionchan.products.rpc.feign;

import com.alibaba.cola.dto.MultiResponse;
import io.github.reionchan.products.dto.ProductDTO;
import io.github.reionchan.products.api.IProductService;
import io.github.reionchan.products.dto.clientobject.ProductWebCO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@Tag(name = "ProductFeignController", description = "产品服务Feign请求端点")
public class ProductFeignController {

    private IProductService productService;

    public ProductFeignController(IProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/rpc/products")
    @Operation(summary = "产品库存列表", description = "显示产品列表并显示库存信息")
    @PreAuthorize("hasRole('user')")
    public MultiResponse<ProductWebCO> getProductsByIds(@RequestParam("ids")
        @Validated @Pattern(regexp = "^[1-9][0-9,]*$", message = "仅支持逗号隔开的非零开头的数字") String ids) {
        Set<Long> idSet = Arrays.stream(ids.split(",")).mapToLong(Long::parseLong)
                .collect(HashSet::new, HashSet::add, HashSet::addAll);
        return productService.getProductsByIds(idSet);
    }
}

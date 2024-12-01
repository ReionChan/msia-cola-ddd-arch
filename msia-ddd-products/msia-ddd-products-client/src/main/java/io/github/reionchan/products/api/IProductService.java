package io.github.reionchan.products.api;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.PageQuery;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import io.github.reionchan.products.dto.clientobject.ProductStockWebCO;
import io.github.reionchan.products.dto.clientobject.ProductWebCO;
import io.github.reionchan.products.dto.command.ProductCreateCmd;
import io.github.reionchan.products.dto.command.ProductModifyCmd;

import java.util.Set;

/**
 * 商品服务接口
 *
 * @author Reion
 * @date 2023-12-15
 **/
public interface IProductService {
    Response add(ProductCreateCmd productCreateCmd);

    Response modify(ProductModifyCmd cmd);

    MultiResponse<ProductWebCO> getProductsByIds(Set<Long> ids);

    PageResponse<ProductStockWebCO> getProductsWithStock(PageQuery query);
}

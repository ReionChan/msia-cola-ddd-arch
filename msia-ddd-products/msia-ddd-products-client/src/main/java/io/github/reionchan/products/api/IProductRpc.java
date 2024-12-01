package io.github.reionchan.products.api;

import com.alibaba.cola.dto.MultiResponse;
import io.github.reionchan.products.dto.ProductDTO;
import io.github.reionchan.products.dto.clientobject.ProductWebCO;

/**
 * 商品服务 RPC 接口
 *
 * @author Reion
 * @date 2023-12-20
 **/
public interface IProductRpc {
    MultiResponse<ProductWebCO> getProductsByIds(String ids);
}

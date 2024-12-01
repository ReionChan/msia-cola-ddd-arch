package io.github.reionchan.products.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.reionchan.products.database.dataobject.ProductDO;
import io.github.reionchan.products.dto.clientobject.ProductStockWebCO;

/**
 * 商品实体映射
 *
 * @author Reion
 * @date 2023-12-15
 **/
public interface ProductMapper extends BaseMapper<ProductDO> {

    IPage<ProductStockWebCO> selectProductWithStock(IPage<ProductStockWebCO> page);
}

package io.github.reionchan.products.repository;

import com.alibaba.cola.dto.PageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.reionchan.products.dto.clientobject.ProductStockWebCO;
import io.github.reionchan.products.dto.clientobject.ProductWebCO;
import io.github.reionchan.products.model.entity.Product;

import java.util.List;
import java.util.Set;

/**
 * @author Reion
 * @date 2024-11-21
 **/
public interface IProductRepository {

    boolean existsProductByBarCode(String barCode);

    boolean save(Product product);

    boolean existsProductById(Long id);

    boolean update(Product product);

    List<ProductWebCO> listByIds(Set<Long> ids);

    IPage<ProductStockWebCO> selectProductWithStock(PageQuery query);
}

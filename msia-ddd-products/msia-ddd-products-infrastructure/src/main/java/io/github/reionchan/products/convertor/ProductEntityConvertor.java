package io.github.reionchan.products.convertor;

import io.github.reionchan.commons.bean.EntityConvertor;
import io.github.reionchan.products.database.dataobject.ProductDO;
import io.github.reionchan.products.dto.clientobject.ProductWebCO;
import io.github.reionchan.products.model.entity.Product;
import org.mapstruct.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductEntityConvertor extends EntityConvertor<Product, ProductDO> {

    @Override
    default void updateDOFromEntity(Product product, @MappingTarget ProductDO dataObject) {
        if(isNotEmpty(product.getId())) {
            dataObject.setUpdateTime(new Date());
        } else {
            dataObject.setCreateTime(new Date());
            dataObject.setUpdateTime(dataObject.getCreateTime());
        }
    }

    ProductWebCO toClientObject(ProductDO productDO);

    List<ProductWebCO> toClientObjectSet(Collection<ProductDO> products);
}

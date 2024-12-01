package io.github.reionchan.products.assembler;

import io.github.reionchan.commons.bean.DTOAssembler;
import io.github.reionchan.products.dto.ProductDTO;
import io.github.reionchan.products.dto.command.ProductCreateCmd;
import io.github.reionchan.products.dto.command.ProductModifyCmd;
import io.github.reionchan.products.model.entity.Product;
import org.mapstruct.*;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductAssembler extends DTOAssembler<ProductDTO, Product> {

    @AfterMapping
    default void afterToEntity(ProductDTO source, @MappingTarget Product product) {

    }

    Product toEntity(ProductCreateCmd cmd);
    Product toEntity(ProductModifyCmd cmd);
}

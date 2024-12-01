package io.github.reionchan.products.assembler;

import io.github.reionchan.commons.bean.DTOAssembler;
import io.github.reionchan.products.dto.StockDTO;
import io.github.reionchan.products.dto.command.StockAddCmd;
import io.github.reionchan.products.dto.command.StockCreateCmd;
import io.github.reionchan.products.model.entity.Stock;
import org.mapstruct.*;

import java.util.Collection;
import java.util.List;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StockAssembler extends DTOAssembler<StockDTO, Stock> {

    @AfterMapping
    default void afterToEntity(StockDTO source, @MappingTarget Stock stock) {

    }

    Stock toEntity(StockCreateCmd cmd);
    Stock toEntity(StockAddCmd cmd);

    List<StockDTO> toDTOList(Collection<Stock> stocks);
}

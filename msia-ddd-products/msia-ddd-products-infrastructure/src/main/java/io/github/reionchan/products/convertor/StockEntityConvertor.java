package io.github.reionchan.products.convertor;

import io.github.reionchan.commons.bean.EntityConvertor;
import io.github.reionchan.products.database.dataobject.StockDO;
import io.github.reionchan.products.model.entity.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Date;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Mapper(componentModel = "spring",
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StockEntityConvertor extends EntityConvertor<Stock, StockDO> {

    @Override
    default void updateDOFromEntity(Stock stock, @MappingTarget StockDO dataObject) {
        if(isNotEmpty(stock.getId())) {
            dataObject.setUpdateTime(new Date());
        } else {
            dataObject.setCreateTime(new Date());
            dataObject.setUpdateTime(dataObject.getCreateTime());
        }
    }
}

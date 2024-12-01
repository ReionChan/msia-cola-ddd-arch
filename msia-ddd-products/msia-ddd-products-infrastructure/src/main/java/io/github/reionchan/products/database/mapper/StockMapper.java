package io.github.reionchan.products.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.reionchan.products.database.dataobject.StockDO;
import io.github.reionchan.products.dto.StockDTO;

/**
 * 库存实体映射
 *
 * @author Reion
 * @date 2023-12-15
 **/
public interface StockMapper extends BaseMapper<StockDO> {
    Integer subStock(StockDO stock);

    StockDO getStockForUpdate(Long id);

    Integer addStock(StockDTO stockDto);
}

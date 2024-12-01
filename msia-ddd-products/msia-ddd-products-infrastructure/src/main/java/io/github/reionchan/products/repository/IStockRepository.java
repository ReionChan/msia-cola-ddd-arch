package io.github.reionchan.products.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.reionchan.products.database.dataobject.StockDO;
import io.github.reionchan.products.dto.StockDTO;
import io.github.reionchan.products.model.entity.Stock;

import java.util.Set;

/**
 * @author Reion
 * @date 2024-11-21
 **/
public interface IStockRepository extends IService<StockDO> {

    boolean existsStockByProductId(Long productId);

    boolean save(Stock stock);

    boolean addStock(Stock stock);

    Set<Stock> subStock(Set<StockDTO> stockDtoSet);
}

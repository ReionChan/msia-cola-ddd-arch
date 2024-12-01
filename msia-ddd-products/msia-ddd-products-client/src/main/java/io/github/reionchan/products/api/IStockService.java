package io.github.reionchan.products.api;

import com.alibaba.cola.dto.Response;
import io.github.reionchan.products.dto.StockDTO;
import io.github.reionchan.products.dto.command.StockAddCmd;
import io.github.reionchan.products.dto.command.StockCreateCmd;

import java.util.List;
import java.util.Set;

/**
 * 库存服务接口
 *
 * @author Reion
 * @date 2023-12-16
 **/
public interface IStockService {
    Response newStock(StockCreateCmd cmd);

    Response addStock(StockAddCmd cmd);

    List<StockDTO> trySubStock(Long orderId, Set<StockDTO> stockDtoSet);
}

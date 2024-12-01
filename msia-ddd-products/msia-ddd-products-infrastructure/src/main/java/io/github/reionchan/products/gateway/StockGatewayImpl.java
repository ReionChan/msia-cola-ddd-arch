package io.github.reionchan.products.gateway;

import io.github.reionchan.products.repository.IStockRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Component
public class StockGatewayImpl implements IStockGateway {
    @Resource
    private IStockRepository stockRepository;
}

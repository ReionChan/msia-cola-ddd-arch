package io.github.reionchan.products.service;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.alibaba.cola.dto.Response;
import io.github.reionchan.products.api.IStockService;
import io.github.reionchan.products.command.StockAddCmdExe;
import io.github.reionchan.products.command.StockCreateCmdExe;
import io.github.reionchan.products.command.StockSubCmdExe;
import io.github.reionchan.products.dto.StockDTO;
import io.github.reionchan.products.dto.command.StockAddCmd;
import io.github.reionchan.products.dto.command.StockCreateCmd;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


/**
 * 库存服务实现类
 *
 * @author Reion
 * @date 2023-12-15
 **/
@Slf4j
@Service
@CatchAndLog
public class StockServiceImpl implements IStockService {

    @Resource
    private StockCreateCmdExe stockCreateCmdExe;
    @Resource
    private StockAddCmdExe stockAddCmdExe;
    @Resource
    private StockSubCmdExe stockSubCmdExe;

    @Override
    public Response newStock(StockCreateCmd cmd) {
        return stockCreateCmdExe.execute(cmd);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response addStock(StockAddCmd cmd) {
        return stockAddCmdExe.execute(cmd);
    }

    @Override
    public List<StockDTO> trySubStock(Long orderId, Set<StockDTO> stockDtoSet) {
        return stockSubCmdExe.execute(orderId, stockDtoSet);
    }
}

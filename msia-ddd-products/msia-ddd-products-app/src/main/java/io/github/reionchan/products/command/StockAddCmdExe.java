package io.github.reionchan.products.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.products.assembler.StockAssembler;
import io.github.reionchan.products.dto.command.StockAddCmd;
import io.github.reionchan.products.extentionpoint.StockAddExtPt;
import io.github.reionchan.products.model.entity.Stock;
import io.github.reionchan.products.repository.IStockRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class StockAddCmdExe {

    @Resource
    private IStockRepository stockRepository;
    @Resource
    private StockAssembler stockAssembler;
    @Resource
    private ExtensionExecutor extensionExecutor;

    public Response execute(StockAddCmd cmd) {
        // 校验
        extensionExecutor.executeVoid(StockAddExtPt.class, cmd.getBizScenario(), chk -> chk.validate(cmd));
        // 转换
        Stock stock = stockAssembler.toEntity(cmd);
        // 业务
        Assert.isTrue(stockRepository.addStock(stock), "添加库存失败");
        log.info("{} 增加 {} 库存成功！", cmd.getId(), cmd.getAmount());
        return Response.buildSuccess();
    }
}

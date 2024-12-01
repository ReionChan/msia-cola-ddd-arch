package io.github.reionchan.products.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.ExtensionExecutor;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.reionchan.products.assembler.ProductAssembler;
import io.github.reionchan.products.assembler.StockAssembler;
import io.github.reionchan.products.dto.command.ProductCreateCmd;
import io.github.reionchan.products.dto.command.StockCreateCmd;
import io.github.reionchan.products.extentionpoint.ProductCreateExtPt;
import io.github.reionchan.products.extentionpoint.StockCreateExtPt;
import io.github.reionchan.products.model.entity.Product;
import io.github.reionchan.products.model.entity.Stock;
import io.github.reionchan.products.repository.IProductRepository;
import io.github.reionchan.products.repository.IStockRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class StockCreateCmdExe {

    @Resource
    private IStockRepository stockRepository;
    @Resource
    private StockAssembler stockAssembler;
    @Resource
    private ExtensionExecutor extensionExecutor;

    public Response execute(StockCreateCmd cmd) {
        // 校验
        extensionExecutor.executeVoid(StockCreateExtPt.class, cmd.getBizScenario(), chk -> chk.validate(cmd));
        // 转换
        Stock stock = stockAssembler.toEntity(cmd);
        // 业务
        Assert.isTrue(stockRepository.save(stock), "库存创建失败");
        return Response.buildSuccess();
    }
}

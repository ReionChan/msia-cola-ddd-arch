package io.github.reionchan.products.extentionpoint.extention;

import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.Extension;
import io.github.reionchan.products.dto.command.StockAddCmd;
import io.github.reionchan.products.dto.command.StockCreateCmd;
import io.github.reionchan.products.extentionpoint.StockAddExtPt;
import io.github.reionchan.products.extentionpoint.StockCreateExtPt;
import io.github.reionchan.products.repository.IStockRepository;
import jakarta.annotation.Resource;

import static io.github.reionchan.products.consts.ProductsBizScenarioCst.PRODUCTS_BIZ_ID;

/**
 * 商品创建前扩展点
 *
 * @author Reion
 * @date 2024-11-20
 **/
@Extension(bizId = PRODUCTS_BIZ_ID)
public class StockExistsExtPt implements StockCreateExtPt, StockAddExtPt {

    @Resource
    private IStockRepository stockRepository;

    @Override
    public void validate(StockCreateCmd cmd) {
        Assert.isTrue(!stockRepository.existsStockByProductId(cmd.getProductId()), "产品已存在库存信息");
    }

    @Override
    public void validate(StockAddCmd cmd) {
        Assert.isTrue(stockRepository.existsStockByProductId(cmd.getId()), "库存信息不存在");
    }
}

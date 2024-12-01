package io.github.reionchan.products.extentionpoint;

import com.alibaba.cola.extension.ExtensionPointI;
import io.github.reionchan.products.dto.command.StockCreateCmd;

/**
 * 库存创建前扩展点
 *
 * @author Reion
 * @date 2024-11-20
 **/
public interface StockCreateExtPt extends ExtensionPointI {

    void validate(StockCreateCmd cmd);
}

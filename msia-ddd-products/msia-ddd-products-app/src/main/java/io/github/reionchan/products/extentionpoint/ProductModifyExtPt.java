package io.github.reionchan.products.extentionpoint;

import com.alibaba.cola.extension.ExtensionPointI;
import io.github.reionchan.products.dto.command.ProductCreateCmd;
import io.github.reionchan.products.dto.command.ProductModifyCmd;

/**
 * 商品创建前扩展点
 *
 * @author Reion
 * @date 2024-11-20
 **/
public interface ProductModifyExtPt extends ExtensionPointI {

    void validate(ProductModifyCmd cmd);
}

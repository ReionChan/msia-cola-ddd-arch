package io.github.reionchan.products.extentionpoint.extention;

import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.Extension;
import io.github.reionchan.products.dto.command.ProductCreateCmd;
import io.github.reionchan.products.dto.command.ProductModifyCmd;
import io.github.reionchan.products.extentionpoint.ProductCreateExtPt;
import io.github.reionchan.products.extentionpoint.ProductModifyExtPt;
import io.github.reionchan.products.repository.IProductRepository;
import jakarta.annotation.Resource;

import static io.github.reionchan.commons.validation.CommonUtil.isEmpty;
import static io.github.reionchan.products.consts.ProductsBizScenarioCst.PRODUCTS_BIZ_ID;

/**
 * 商品创建前扩展点
 *
 * @author Reion
 * @date 2024-11-20
 **/
@Extension(bizId = PRODUCTS_BIZ_ID)
public class ProductExistsExtPt implements ProductCreateExtPt, ProductModifyExtPt {

    @Resource
    private IProductRepository productRepository;

    @Override
    public void validate(ProductCreateCmd cmd) {
        Assert.isTrue(!productRepository.existsProductByBarCode(cmd.getBarCode()), "商品条码已存在");
    }

    @Override
    public void validate(ProductModifyCmd cmd) {
        Assert.isFalse(isEmpty(cmd.getName()) && isEmpty(cmd.getPrice()) && isEmpty(cmd.getBarCode()), "未做任何修改");
        Assert.isTrue(productRepository.existsProductById(cmd.getId()), "商品不存在");
    }
}

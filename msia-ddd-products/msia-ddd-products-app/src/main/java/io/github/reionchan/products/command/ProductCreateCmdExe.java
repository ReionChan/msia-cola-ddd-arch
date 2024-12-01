package io.github.reionchan.products.command;

import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.products.assembler.ProductAssembler;
import io.github.reionchan.products.dto.command.ProductCreateCmd;
import io.github.reionchan.products.extentionpoint.ProductCreateExtPt;
import io.github.reionchan.products.model.entity.Product;
import io.github.reionchan.products.repository.IProductRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class ProductCreateCmdExe {

    @Resource
    private IProductRepository productRepository;
    @Resource
    private ProductAssembler productAssembler;
    @Resource
    private ExtensionExecutor extensionExecutor;

    public SingleResponse<Long> execute(ProductCreateCmd cmd) {
        // 校验
        extensionExecutor.executeVoid(ProductCreateExtPt.class, cmd.getBizScenario(), chk -> chk.validate(cmd));
        // 转换
        Product product = productAssembler.toEntity(cmd);
        // 业务
        Assert.isTrue(productRepository.save(product), "商品创建失败");
        return SingleResponse.of(product.getId());
    }
}

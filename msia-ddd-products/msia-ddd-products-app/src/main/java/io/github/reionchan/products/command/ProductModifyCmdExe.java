package io.github.reionchan.products.command;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.alibaba.cola.exception.Assert;
import com.alibaba.cola.extension.ExtensionExecutor;
import io.github.reionchan.products.assembler.ProductAssembler;
import io.github.reionchan.products.database.dataobject.ProductDO;
import io.github.reionchan.products.dto.command.ProductModifyCmd;
import io.github.reionchan.products.extentionpoint.ProductCreateExtPt;
import io.github.reionchan.products.extentionpoint.ProductModifyExtPt;
import io.github.reionchan.products.model.entity.Product;
import io.github.reionchan.products.repository.IProductRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class ProductModifyCmdExe {

    @Resource
    private IProductRepository productRepository;
    @Resource
    private ProductAssembler productAssembler;
    @Resource
    private ExtensionExecutor extensionExecutor;

    public Response execute(ProductModifyCmd cmd) {
        // 校验
        extensionExecutor.executeVoid(ProductModifyExtPt.class, cmd.getBizScenario(), chk -> chk.validate(cmd));
        // 转换
        Product product = productAssembler.toEntity(cmd);
        // 业务
        Assert.isTrue(productRepository.update(product), "商品修改失败");
        return Response.buildSuccess();
    }
}

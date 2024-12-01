package io.github.reionchan.products.service;

import com.alibaba.cola.catchlog.CatchAndLog;
import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.PageQuery;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import io.github.reionchan.products.api.IProductService;
import io.github.reionchan.products.command.ProductCreateCmdExe;
import io.github.reionchan.products.command.ProductModifyCmdExe;
import io.github.reionchan.products.command.query.ProductIdsQryExe;
import io.github.reionchan.products.command.query.ProductPageQryExe;
import io.github.reionchan.products.dto.clientobject.ProductStockWebCO;
import io.github.reionchan.products.dto.clientobject.ProductWebCO;
import io.github.reionchan.products.dto.command.ProductCreateCmd;
import io.github.reionchan.products.dto.command.ProductModifyCmd;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 商品服务实现类
 *
 * @author Reion
 * @date 2023-12-15
 **/
@Slf4j
@Service
@CatchAndLog
public class ProductServiceImpl implements IProductService {

    @Resource
    private ProductCreateCmdExe productCreateCmdExe;
    @Resource
    private ProductModifyCmdExe productModifyCmdExe;
    @Resource
    private ProductIdsQryExe productIdsQryExe;
    @Resource
    private ProductPageQryExe productPageQryExe;

    @Override
    public Response add(ProductCreateCmd productCreateCmd) {
        return productCreateCmdExe.execute(productCreateCmd);
    }

    @Override
    public Response modify(ProductModifyCmd cmd) {
        return productModifyCmdExe.execute(cmd);
    }

    @Override
    @Cacheable(cacheNames = "productsByIds")
    public MultiResponse<ProductWebCO> getProductsByIds(Set<Long> ids) {
        return productIdsQryExe.execute(ids);
    }

    @Override
    public PageResponse<ProductStockWebCO> getProductsWithStock(PageQuery query) {
        return productPageQryExe.execute(query);
    }
}

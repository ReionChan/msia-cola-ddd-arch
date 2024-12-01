package io.github.reionchan.products.command.query;

import com.alibaba.cola.dto.PageQuery;
import com.alibaba.cola.dto.PageResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.reionchan.products.dto.clientobject.ProductStockWebCO;
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
public class ProductPageQryExe {

    @Resource
    private IProductRepository productRepository;

    public PageResponse<ProductStockWebCO> execute(PageQuery query) {
        // 业务
        IPage<ProductStockWebCO> page = productRepository.selectProductWithStock(query);
        return PageResponse.of(page.getRecords(), (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
    }
}

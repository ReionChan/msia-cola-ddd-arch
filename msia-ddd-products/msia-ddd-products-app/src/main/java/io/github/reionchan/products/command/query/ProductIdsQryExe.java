package io.github.reionchan.products.command.query;

import com.alibaba.cola.dto.MultiResponse;
import io.github.reionchan.products.dto.clientobject.ProductWebCO;
import io.github.reionchan.products.repository.IProductRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static io.github.reionchan.commons.validation.CommonUtil.isEmpty;

/**
 * @author Reion
 * @date 2024-11-16
 **/
@Slf4j
@Component
public class ProductIdsQryExe {

    @Resource
    private IProductRepository productRepository;

    public MultiResponse<ProductWebCO> execute(Set<Long> ids) {
        // 校验
        // 转换
        // 业务
        List<ProductWebCO> products = productRepository.listByIds(ids);
        if (isEmpty(products)) {
            return MultiResponse.buildFailure("emptyData", "未查询到商品");
        }
        return MultiResponse.of(products);
    }
}

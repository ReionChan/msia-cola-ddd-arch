package io.github.reionchan.products.repository.impl;

import com.alibaba.cola.dto.PageQuery;
import com.alibaba.cola.exception.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.reionchan.core.util.ConditionUtil;
import io.github.reionchan.products.convertor.ProductEntityConvertor;
import io.github.reionchan.products.database.dataobject.ProductDO;
import io.github.reionchan.products.database.mapper.ProductMapper;
import io.github.reionchan.products.dto.clientobject.ProductStockWebCO;
import io.github.reionchan.products.dto.clientobject.ProductWebCO;
import io.github.reionchan.products.model.entity.Product;
import io.github.reionchan.products.repository.IProductRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Repository
public class ProductRepositoryImpl implements IProductRepository {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProductEntityConvertor productEntityConvertor;

    @Override
    public boolean existsProductByBarCode(String barCode) {
        Assert.isTrue(isNotEmpty(barCode), "条码为空");
        QueryWrapper<ProductDO> queryWrapper = new QueryWrapper<>();
        return productMapper.exists(queryWrapper.lambda().eq(ProductDO::getBarCode, barCode));
    }

    @Override
    public boolean existsProductById(Long id) {
        Assert.isTrue(isNotEmpty(id), "ID为空");
        QueryWrapper<ProductDO> queryWrapper = new QueryWrapper<>();
        return productMapper.exists(queryWrapper.lambda().eq(ProductDO::getId, id));
    }

    @Override
    public boolean save(Product product) {
        ProductDO productDO = productEntityConvertor.toDataObject(product);
        boolean ret = productMapper.insert(productDO)>0;
        if (ret) {
            product.setId(productDO.getId());
        }
        return ret;
    }

    @Override
    public boolean update(Product product) {
        ProductDO productDO = productEntityConvertor.toDataObject(product);
        return productMapper.updateById(productDO)>0;
    }

    @Override
    public List<ProductWebCO> listByIds(Set<Long> ids) {
        List<ProductDO> productDOList = productMapper.selectBatchIds(ids);
        return isNotEmpty(productDOList) ? productEntityConvertor.toClientObjectSet(productDOList) : null;
    }

    @Override
    public IPage<ProductStockWebCO> selectProductWithStock(PageQuery query) {
        return productMapper.selectProductWithStock(ConditionUtil.getPage(query));
    }
}

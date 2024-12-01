package io.github.reionchan.products.repository.impl;

import com.alibaba.cola.exception.Assert;
import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.reionchan.products.convertor.StockEntityConvertor;
import io.github.reionchan.products.database.dataobject.StockDO;
import io.github.reionchan.products.database.mapper.StockMapper;
import io.github.reionchan.products.dto.StockDTO;
import io.github.reionchan.products.model.entity.Stock;
import io.github.reionchan.products.repository.IStockRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static io.github.reionchan.commons.validation.CommonUtil.isEmpty;
import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Slf4j
@Repository
public class StockRepositoryImpl extends ServiceImpl<StockMapper, StockDO> implements IStockRepository {

    @Resource
    private StockMapper stockMapper;
    @Resource
    private StockEntityConvertor stockEntityConvertor;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public boolean existsStockByProductId(Long productId) {
        Assert.isTrue(isNotEmpty(productId), "商品ID为空");
        QueryWrapper<StockDO> queryWrapper = new QueryWrapper<>();
        return stockMapper.exists(queryWrapper.lambda().eq(StockDO::getProductId, productId));
    }

    @Override
    public boolean save(Stock stock) {
        StockDO stockDO = stockEntityConvertor.toDataObject(stock);
        boolean flag = stockMapper.insert(stockDO)>0;
        if (flag) {
            stock.setId(stockDO.getId());
        }
        return flag;
    }

    @Override
    public boolean addStock(Stock stock) {
        StockDO stockDO = stockMapper.getStockForUpdate(stock.getId());
        stockDO.setAmount(stockDO.getAmount() + stock.getAmount());
        stockDO.setUpdateTime(new Date());
        return stockMapper.updateById(stockDO)>0;
    }

    @Override
    public Set<Stock> subStock(Set<StockDTO> stockDtoSet) {
        List<StockDO> toBeSubStocks = stockDtoSet.stream().map(dto -> {
            StockDO stock = new StockDO();
            stock.setProductId(dto.getProductId());
            stock.setAmount(dto.getAmount());
            return stock;
        }).sorted(Comparator.comparing(StockDO::getProductId)).collect(Collectors.toList());

        Set<Stock> outOfStockSet = new HashSet<>();
        try {
            transactionTemplate.execute(status -> {
                // 批量提交时，按照商品ID顺序执行 UPDATE 操作，尽量避免死锁概率
                MybatisBatch<StockDO> batch = new MybatisBatch<>(getSqlSessionFactory(), toBeSubStocks);
                MybatisBatch.Method<StockDO> method = new MybatisBatch.Method<>(StockMapper.class);

                List<BatchResult> results = batch.execute(method.get("subStock"));
                BatchResult result = results.get(0);
                int[] resultCounts = result.getUpdateCounts();
                log.debug("库存扣减 SQL:\n\t{}\nCOUNTS:\n\t{}", result.getSql(), resultCounts);
                for (int i = 0; i < resultCounts.length; i++) {
                    if (resultCounts[i] < 1) {
                        outOfStockSet.add(stockEntityConvertor.toEntity(toBeSubStocks.get(i)));
                    }
                }
                Assert.isTrue(isEmpty(outOfStockSet), "库存不足");
                return outOfStockSet;
            });
        } catch (Exception e) {
            log.warn("库存扣减失败", e);
        }
        return outOfStockSet;
    }
}

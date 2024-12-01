package io.github.reionchan.orders.repository.impl;

import com.alibaba.cola.exception.Assert;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.reionchan.orders.convertor.OrderDetailEntityConvertor;
import io.github.reionchan.orders.database.dataobject.OrderDetailDO;
import io.github.reionchan.orders.database.mapper.OrderDetailMapper;
import io.github.reionchan.orders.model.entity.OrderDetail;
import io.github.reionchan.orders.repository.IOrderDetailRepository;
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
public class OrderDetailRepositoryImpl extends ServiceImpl<OrderDetailMapper, OrderDetailDO> implements IOrderDetailRepository {

    @Resource
    private OrderDetailMapper detailMapper;
    @Resource
    private OrderDetailEntityConvertor detailEntityConvertor;

    @Override
    public Set<OrderDetail> getOrderDetailList(Long orderId) {
        Assert.notNull(orderId, "订单ID为空");
        Wrapper<OrderDetailDO> query = new QueryWrapper<OrderDetailDO>().lambda().eq(OrderDetailDO::getOrderId, orderId);
        List<OrderDetailDO> detailDOList = detailMapper.selectList(query);
        return isNotEmpty(detailDOList) ? detailEntityConvertor.toDataEntitySet(detailDOList) : null;
    }
}

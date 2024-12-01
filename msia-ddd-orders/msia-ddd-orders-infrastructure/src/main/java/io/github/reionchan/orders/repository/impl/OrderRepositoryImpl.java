package io.github.reionchan.orders.repository.impl;

import com.alibaba.cola.exception.Assert;
import io.github.reionchan.orders.convertor.OrderEntityConvertor;
import io.github.reionchan.orders.database.dataobject.OrderDO;
import io.github.reionchan.orders.database.mapper.OrderMapper;
import io.github.reionchan.orders.model.entity.Order;
import io.github.reionchan.orders.model.vo.OrderStatus;
import io.github.reionchan.orders.repository.IOrderRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import static io.github.reionchan.commons.validation.CommonUtil.isNotEmpty;

/**
 * @author Reion
 * @date 2024-11-21
 **/
@Repository
public class OrderRepositoryImpl implements IOrderRepository {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderEntityConvertor orderEntityConvertor;

    @Override
    public Order getOrderById(Long orderId) {
        Assert.notNull(orderId, "订单ID为空");
        OrderDO orderDO = orderMapper.selectById(orderId);
        return isNotEmpty(orderDO) ? orderEntityConvertor.toEntity(orderDO) : null;
    }

    @Override
    public boolean save(Order order) {
        OrderDO orderDO = orderEntityConvertor.toDataObject(order);
        orderDO.setStatus(OrderStatus.UNPAID.getValue());
        boolean flag = orderMapper.insert(orderDO) > 0;
        if (flag) {
            order.setId(orderDO.getId());
        }
        return flag;
    }

    @Override
    public boolean update(Order order) {
        OrderDO orderDO = orderEntityConvertor.toDataObject(order);
        return orderMapper.updateById(orderDO)>0;
    }
}

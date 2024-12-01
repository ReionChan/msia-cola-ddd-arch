package io.github.reionchan.orders.database.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.reionchan.orders.database.dataobject.OrderDO;

/**
 * 订单实体映射
 *
 * @author Reion
 * @date 2023-12-16
 **/
public interface OrderMapper extends BaseMapper<OrderDO> {
    OrderDO getOrderForUpdate(Long id);
}

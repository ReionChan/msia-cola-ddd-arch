package io.github.reionchan.orders.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.reionchan.orders.database.dataobject.OrderDetailDO;
import io.github.reionchan.orders.model.entity.OrderDetail;

import java.util.Set;

/**
 * @author Reion
 * @date 2024-11-21
 **/
public interface IOrderDetailRepository extends IService<OrderDetailDO> {

    Set<OrderDetail> getOrderDetailList(Long orderId);
}

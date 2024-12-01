package io.github.reionchan.orders.api;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import io.github.reionchan.orders.dto.OrderCreationDTO;
import io.github.reionchan.orders.dto.clientobject.OrderWebCO;
import io.github.reionchan.orders.dto.command.OrderCreationCmd;
import io.github.reionchan.orders.dto.command.OrderModifyCmd;

/**
 * 订单服务
 *
 * @author Reion
 * @date 2023-12-16
 **/
public interface IOrderService {
    SingleResponse<Long> createOrder(OrderCreationCmd cmd);

    Response modifyOrderDetail(OrderModifyCmd batchModifyCmd);

    SingleResponse<OrderWebCO> getOrder(Long id, boolean isAdmin);

    Response deleteOrder(Long id);

    Response modifyOrderStatus(Long id, Byte status);

    Response payOrder(Long id);
}
